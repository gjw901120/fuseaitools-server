package com.fuse.ai.server.web.common.utils;

import com.fuse.ai.server.web.common.enums.RedisKeysEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RDAP 域名校验器。
 *
 * <p>用于发送邮箱验证码前的防刷校验：除特殊邮箱(如 qq.com/163.com/gmail.com 等)外，
 * 其余域名邮箱会调用 Verisign RDAP 查询域名的注册时间(registration)与最后变更时间(last changed)。
 * 若其中任一事件发生在阈值天数(默认100天)以内，判定为"新注册/近期变更"域名，拒绝发送验证码，
 * 防止批量注册域名刷注册福利。</p>
 *
 * <p>说明：Verisign RDAP 仅覆盖 .com/.net 后缀；其他后缀域名无法校验时会放行并记录告警日志。</p>
 */
@Slf4j
@Component
public class RdapDomainValidator {

    /** RDAP 校验结果 */
    private enum CheckResult {
        PASS,          // 域名存在且注册/变更时间超过阈值 -> 放行
        BLOCK,         // 域名注册或变更时间在阈值内 -> 拦截
        NOT_FOUND,     // 域名在注册库中不存在 -> 继续尝试父域
        NOT_SUPPORTED  // 非 Verisign 管理的后缀(.com/.net)，无法校验
    }

    /** Verisign RDAP 支持的后缀 */
    private static final List<String> SUPPORTED_TLDS = Collections.unmodifiableList(Arrays.asList("com", "net"));

    /** Redis 缓存结果值 */
    private static final String RESULT_PASS = "PASS";
    private static final String RESULT_BLOCK = "BLOCK";
    private static final String RESULT_NOT_FOUND = "NOT_FOUND";

    /** 缓存 TTL（秒） */
    private static final long CACHE_TTL_PASS_SECONDS = 6 * 60 * 60;    // 6 小时
    private static final long CACHE_TTL_BLOCK_SECONDS = 24 * 60 * 60;  // 24 小时

    private final RestTemplate restTemplate;

    @Autowired
    private RedisUtil redisUtil;

    /** 是否开启域名新注册校验 */
    @Value("${app.code.rdap-check-enabled:true}")
    private boolean rdapCheckEnabled;

    /** 域名注册/变更时间阈值（天），阈值内视为新域名 */
    @Value("${app.code.rdap-min-age-days:100}")
    private int minAgeDays;

    /** Verisign RDAP .com 查询地址（.net 由该地址推导） */
    @Value("${app.code.rdap-base-url:https://rdap.verisign.com/com/v1/domain/}")
    private String rdapBaseUrl;

    /** 特殊邮箱域名白名单（逗号分隔），白名单域名不校验直接放行 */
    @Value("${app.code.rdap-whitelist-domains:qq.com,163.com,gmail.com,outlook.com,hotmail.com,icloud.com,126.com,foxmail.com,yahoo.com}")
    private String whitelistDomains;

    public RdapDomainValidator() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 校验发送验证码的邮箱域名，不通过时抛出异常。
     *
     * @param email 目标邮箱
     */
    public void checkEmailDomain(String email) {
        if (!rdapCheckEnabled) {
            return;
        }
        String domain = extractDomain(email);
        if (domain == null || isWhitelisted(domain)) {
            return;
        }

        // 候选域名：完整域名 -> 逐级父域（处理 mail.xxx.com 这类子域邮箱）
        List<String> candidates = candidateDomains(domain);
        boolean checkedAny = false; // 是否至少有一个候选域名成功执行了注册库查询
        for (String candidate : candidates) {
            CheckResult result = checkCandidate(candidate);
            if (result == CheckResult.PASS) {
                return; // 域名存在且为老域名，放行
            }
            if (result == CheckResult.BLOCK) {
                log.info("RDAP check blocked, domain {} is newly registered/changed", candidate);
                throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR,
                        "Your email is invalid. Please use a different email and try again later.");
            }
            if (result == CheckResult.NOT_FOUND) {
                checkedAny = true; // 该层域名未注册，继续尝试父域
            }
            // NOT_SUPPORTED：非 .com/.net 后缀，无法校验，继续看下一候选
        }

        if (checkedAny) {
            // 所有可校验的候选域名在注册库中均不存在 -> 邮箱域名虚构，拒绝发送
            log.warn("RDAP check: no registered domain found for email domain: {}", domain);
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR,
                    "The email domain is not valid. Please use another email and try again later");
        }

        // 域名后缀不在 Verisign 支持范围(如 .org/.io/.xyz 等)，无法校验 -> 放行并告警，便于运营后续关注
        log.warn("RDAP check skipped, domain suffix not supported by verisign: {}", domain);
    }

    /**
     * 对单个候选域名执行校验（含 Redis 缓存）。
     */
    private CheckResult checkCandidate(String domain) {
        String cacheKey = RedisKeysEnum.RDAP_DOMAIN_CHECK.format(domain.toLowerCase(Locale.ROOT));
        Object cached = redisUtil.get(cacheKey);
        if (cached != null) {
            return toCheckResult(cached.toString());
        }

        String tld = tldOf(domain);
        if (!SUPPORTED_TLDS.contains(tld)) {
            return CheckResult.NOT_SUPPORTED;
        }
        // .net 与 .com 同为 Verisign 管理，使用对应的 .net RDAP 端点
        String baseUrl = "net".equals(tld) ? rdapBaseUrl.replace("/com/", "/net/") : rdapBaseUrl;
        String url = baseUrl + domain;

        CheckResult result = queryAndJudge(url, domain);
        // 写入缓存
        switch (result) {
            case PASS:
                redisUtil.set(cacheKey, RESULT_PASS, CACHE_TTL_PASS_SECONDS, TimeUnit.SECONDS);
                break;
            case BLOCK:
                redisUtil.set(cacheKey, RESULT_BLOCK, CACHE_TTL_BLOCK_SECONDS, TimeUnit.SECONDS);
                break;
            case NOT_FOUND:
                redisUtil.set(cacheKey, RESULT_NOT_FOUND, CACHE_TTL_PASS_SECONDS, TimeUnit.SECONDS);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 调用 RDAP 查询并判定域名注册时间/最后变更时间。
     */
    private CheckResult queryAndJudge(String url, String domain) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("RDAP query returned non-2xx status: url={}, status={}", url, response.getStatusCode());
                return CheckResult.PASS; // 无法确认的视为放行，避免误伤正常用户
            }
            return judgeEvents(response.getBody(), domain);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.info("RDAP 404, domain not found in registry: {}", domain);
                return CheckResult.NOT_FOUND;
            }
            log.error("RDAP query failed with client error: url={}, status={}, body={}",
                    url, e.getStatusCode(), e.getResponseBodyAsString());
            return CheckResult.PASS;
        } catch (RestClientException e) {
            log.error("RDAP query failed: url={}", url, e);
            return CheckResult.PASS; // 网络异常放行，避免影响正常用户体验
        }
    }

    /**
     * 解析 RDAP events，判定注册时间(registration)与最后变更时间(last changed)。
     * 任一时间点发生在阈值天数内即视为需拦截的新域名。
     */
    private CheckResult judgeEvents(Map<String, Object> body, String domain) {
        Object eventsObj = body.get("events");
        if (!(eventsObj instanceof List)) {
            log.warn("RDAP response has no events: domain={}", domain);
            return CheckResult.PASS;
        }

        Instant now = Instant.now();
        Instant registrationDate = null;
        Instant lastChangedDate = null;
        for (Object item : (List<?>) eventsObj) {
            if (!(item instanceof Map)) {
                continue;
            }
            Map<?, ?> event = (Map<?, ?>) item;
            Object actionObj = event.get("eventAction");
            Object dateObj = event.get("eventDate");
            if (actionObj == null || dateObj == null) {
                continue;
            }
            Instant date = parseDate(dateObj.toString());
            if (date == null) {
                continue;
            }
            if ("registration".equalsIgnoreCase(actionObj.toString())) {
                registrationDate = date;
            } else if ("last changed".equalsIgnoreCase(actionObj.toString())) {
                lastChangedDate = date;
            }
        }

        if (registrationDate != null && isNewerThanThreshold(registrationDate, now)) {
            log.info("RDAP block: domain={} registrationDate={} within {} days", domain, registrationDate, minAgeDays);
            return CheckResult.BLOCK;
        }
        if (lastChangedDate != null && isNewerThanThreshold(lastChangedDate, now)) {
            log.info("RDAP block: domain={} lastChangedDate={} within {} days", domain, lastChangedDate, minAgeDays);
            return CheckResult.BLOCK;
        }
        return CheckResult.PASS;
    }

    private boolean isNewerThanThreshold(Instant date, Instant now) {
        return date.isAfter(now.minus(minAgeDays, ChronoUnit.DAYS));
    }

    /**
     * 从邮箱中提取域名部分。
     */
    private String extractDomain(String email) {
        if (email == null) {
            return null;
        }
        String lower = email.trim().toLowerCase(Locale.ROOT);
        int idx = lower.lastIndexOf('@');
        if (idx < 0 || idx == lower.length() - 1) {
            return null;
        }
        String domain = lower.substring(idx + 1).trim();
        while (domain.endsWith(".")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        return domain.isEmpty() ? null : domain;
    }

    /**
     * 生成候选域名列表：完整域名 + 逐级父域，至少保留两级(注册域)。
     */
    private List<String> candidateDomains(String domain) {
        String[] labels = domain.split("\\.");
        if (labels.length <= 2) {
            return Collections.singletonList(domain);
        }
        List<String> candidates = new ArrayList<>();
        for (int i = 0; i <= labels.length - 2; i++) {
            candidates.add(String.join(".", Arrays.copyOfRange(labels, i, labels.length)));
        }
        return candidates;
    }

    private boolean isWhitelisted(String domain) {
        for (String w : whitelistDomains.split(",")) {
            String wd = w.trim().toLowerCase(Locale.ROOT);
            if (wd.isEmpty()) {
                continue;
            }
            if (domain.equals(wd) || domain.endsWith("." + wd)) {
                return true;
            }
        }
        return false;
    }

    private String tldOf(String domain) {
        int idx = domain.lastIndexOf('.');
        return idx < 0 ? "" : domain.substring(idx + 1);
    }

    private Instant parseDate(String dateStr) {
        try {
            return Instant.parse(dateStr);
        } catch (Exception e) {
            log.warn("Parse RDAP eventDate failed: {}", dateStr);
            return null;
        }
    }

    private CheckResult toCheckResult(String cached) {
        if (RESULT_BLOCK.equals(cached)) {
            return CheckResult.BLOCK;
        }
        if (RESULT_NOT_FOUND.equals(cached)) {
            return CheckResult.NOT_FOUND;
        }
        return CheckResult.PASS;
    }
}
