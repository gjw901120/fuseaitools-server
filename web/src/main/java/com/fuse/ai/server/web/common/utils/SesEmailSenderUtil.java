package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SesEmailSenderUtil {

    @Value("${aws.ses.region}")
    private String region;

    @Value("${aws.ses.credentials.access-key}")
    private String accessKey;

    @Value("${aws.ses.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.email.default-from}")
    private String from;

    private SesV2Client client;

    @PostConstruct
    private void initClient() {
        try {
            this.client = SesV2Client.builder()
                    .region(Region.of(this.region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(this.accessKey, this.secretKey)
                    ))
                    .build();
            log.info("SES v2客户端初始化成功，区域: {}", region);
        } catch (Exception e) {
            log.error("SES客户端初始化失败", e);
            throw new BaseException(ThirdpartyErrorType.EMAIL_NOTIFICATION_SERVER_ERROR,
                    "SES客户端初始化失败: " + e.getMessage());
        }
    }

    public String sendEmail(String to, String subject, String textContent) {
        return sendEmail(to, subject, textContent, null);
    }

    public String sendEmail(String to, String subject, String textContent, String htmlContent) {
        try {
            // 构建邮件内容
            Body.Builder bodyBuilder = Body.builder();

            if (textContent != null && !textContent.trim().isEmpty()) {
                bodyBuilder.text(Content.builder()
                        .data(textContent)
                        .charset("UTF-8")
                        .build());
            }

            if (htmlContent != null && !htmlContent.trim().isEmpty()) {
                bodyBuilder.html(Content.builder()
                        .data(htmlContent)
                        .charset("UTF-8")
                        .build());
            }

            SendEmailRequest request = SendEmailRequest.builder()
                    .fromEmailAddress(this.from)
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .content(EmailContent.builder()
                            .simple(Message.builder()
                                    .subject(Content.builder()
                                            .data(subject)
                                            .charset("UTF-8")
                                            .build())
                                    .body(bodyBuilder.build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = this.client.sendEmail(request);
            log.info("邮件发送成功: messageId={}, to={}", response.messageId(), to);

            return response.messageId();

        } catch (Exception e) {
            log.error("邮件发送失败: to={}, subject={}", to, subject, e);
            throw new BaseException(ThirdpartyErrorType.EMAIL_NOTIFICATION_SERVER_ERROR,
                    "邮件发送失败: " + e.getMessage());
        }
    }
}