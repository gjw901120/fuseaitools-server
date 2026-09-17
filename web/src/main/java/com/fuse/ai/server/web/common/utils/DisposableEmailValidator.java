package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 一次性邮箱域名校验器。
 *
 * <p>在发送邮箱验证码前，校验邮箱域名是否属于已知的一次性邮箱服务（如 tempmail、guerrillamail 等）。
 * 域名列表来源于 <a href="https://github.com/disposable/disposable-email-domains">disposable-email-domains</a>，
 * 存放在 classpath:disposable-email-domains.txt 中。</p>
 */
@Slf4j
@Component
public class DisposableEmailValidator {

    private static final String DOMAIN_LIST_PATH = "disposable-email-domains.txt";

    /** 是否开启一次性邮箱校验 */
    @Value("${app.code.disposable-check-enabled:true}")
    private boolean disposableCheckEnabled;

    private Set<String> disposableDomains;

    @PostConstruct
    public void init() {
        disposableDomains = loadDomains();
        log.info("Disposable email domains loaded, count={}", disposableDomains.size());
    }

    /**
     * 校验邮箱域名是否为一次性邮箱，若是则抛出异常。
     *
     * @param email 目标邮箱
     */
    public void checkDisposableEmail(String email) {
        if (!disposableCheckEnabled) {
            return;
        }
        String domain = extractDomain(email);
        if (domain == null) {
            return;
        }
        if (disposableDomains.contains(domain)) {
            log.warn("Disposable email blocked: {}", email);
            throw new BaseException(UserErrorType.VERIFICATION_CODE_ERROR,
                    "There is an anomaly with your email address. Please use a different email.");
        }
    }

    private String extractDomain(String email) {
        if (email == null) {
            return null;
        }
        String lower = email.trim().toLowerCase(Locale.ROOT);
        int idx = lower.lastIndexOf('@');
        if (idx < 0 || idx == lower.length() - 1) {
            return null;
        }
        return lower.substring(idx + 1).trim();
    }

    private Set<String> loadDomains() {
        Set<String> domains = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(DOMAIN_LIST_PATH).getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim().toLowerCase(Locale.ROOT);
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    domains.add(trimmed);
                }
            }
        } catch (Exception e) {
            log.error("Failed to load disposable email domains from {}", DOMAIN_LIST_PATH, e);
        }
        return domains;
    }
}
