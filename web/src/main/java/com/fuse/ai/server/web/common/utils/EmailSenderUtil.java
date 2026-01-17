package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.annotation.PostConstruct;

@Component
public class EmailSenderUtil {

    // 直接从YAML注入，仅此而已
    @Value("${aws.ses.region}")
    private String region;

    @Value("${aws.ses.credentials.access-key}")
    private String accessKey;

    @Value("${aws.ses.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.email.default-from}")
    private String from;

    @Value("${aws.email.default-charset:UTF-8}")
    private String charset;

    private SesClient client;

    @PostConstruct
    private void initClient() {
        // 构建客户端：凭证100%来自YAML配置
        this.client = SesClient.builder()
                .region(Region.of(this.region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(this.accessKey, this.secretKey)
                ))
                .build();
    }

    /**
     * 发送邮件
     * @param to 收件邮箱
     * @param subject 邮件主题
     * @param content 邮件正文
     * @return 消息ID
     */
    public String sendEmail(String to, String subject, String content) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(this.from)
                    .destination(Destination.builder().toAddresses(to).build())
                    .message(Message.builder()
                            .subject(Content.builder().charset(this.charset).data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().charset(this.charset).data(content).build())
                                    .build())
                            .build())
                    .build();

            return this.client.sendEmail(request).messageId();
        } catch (Exception e) {
            throw new BaseException(ThirdpartyErrorType.EMAIL_NOTIFICATION_SERVER_ERROR, e.getMessage());
        }
    }
}