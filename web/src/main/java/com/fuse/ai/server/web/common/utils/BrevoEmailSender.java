package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BrevoEmailSender {

    @Value("${brevo.sender.apiKey}")
    private String apiKey;

    private RestTemplate restTemplate;
    private HttpHeaders headers;

    private final EmailTemplateBuilder emailTemplateBuilder;

    @PostConstruct
    private void init() {
        log.info("初始化Brevo API邮件发送器");
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.headers.set("api-key", apiKey);
    }

    public BrevoEmailSender(EmailTemplateBuilder emailTemplateBuilder) {
        this.emailTemplateBuilder = emailTemplateBuilder;
    }


    /**
     * 使用Brevo API发送验证码邮件（支持HTML和纯文本）
     */
    public void sendEmail(String to, String verificationCode, int codeExpireMinutes) {
        try {
            Map<String, Object> request = new HashMap<>();

            // 发件人
            Map<String, String> sender = new HashMap<>();
            sender.put("email", "no-reply@fuseaitools.com");
            sender.put("name", "FuseAI Service");
            request.put("sender", sender);

            // 收件人
            Map<String, Object> recipient = new HashMap<>();
            recipient.put("email", to);
            request.put("to", List.of(recipient));

            // 邮件主题
            request.put("subject", "FuseAI - Email Verification Code");

            // HTML内容（使用EmailTemplateBuilder生成）
            String htmlContent = emailTemplateBuilder.buildVerificationEmailHtml(
                    verificationCode, codeExpireMinutes, to);
            request.put("htmlContent", htmlContent);

            // 纯文本备用内容
            String textContent = emailTemplateBuilder.buildVerificationEmailText(
                    verificationCode, codeExpireMinutes);
            request.put("textContent", textContent);

            // 邮件标签和跟踪设置
            request.put("tags", List.of("verification", "authentication"));

            // 添加跟踪设置
            Map<String, Boolean> tracking = new HashMap<>();
            tracking.put("click", true);
            tracking.put("open", true);
            request.put("tracking", tracking);

            // 发送请求到Brevo API
            String url = "https://api.brevo.com/v3/smtp/email";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class);

            // 处理响应
            handleApiResponse(response, to, verificationCode);

        } catch (Exception e) {
            log.error("Brevo API邮件发送失败: to={}, code={}", to, verificationCode, e);
            throw new BaseException(ThirdpartyErrorType.EMAIL_NOTIFICATION_SERVER_ERROR,
                    "邮件发送失败: " + e.getMessage());
        }
    }

    /**
     * 处理API响应
     */
    private void handleApiResponse(ResponseEntity<Map> response, String to, String verificationCode) {
        if (response.getStatusCode() == HttpStatus.CREATED) {
            Map<String, Object> body = response.getBody();
            if (body != null) {
                String messageId = (String) body.get("messageId");
                log.info("Brevo API邮件发送成功: messageId={}, to={}, code={}",
                        messageId, to, verificationCode);

                // 可选：记录更详细的发送信息
                if (log.isDebugEnabled()) {
                    log.debug("邮件发送详情: {}", body);
                }
            } else {
                log.warn("Brevo API返回成功但响应体为空: to={}", to);
            }
        } else {
            log.error("Brevo API返回错误状态: status={}, body={}",
                    response.getStatusCode(), response.getBody());
            throw new RuntimeException("Brevo API错误: " + response.getStatusCode());
        }
    }
}