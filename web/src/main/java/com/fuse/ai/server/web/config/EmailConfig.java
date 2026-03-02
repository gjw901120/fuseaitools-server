package com.fuse.ai.server.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@Configuration
@EnableScheduling
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 配置SMTP服务器
        mailSender.setHost("smtp-relay.brevo.com");
        mailSender.setPort(587);
        mailSender.setUsername("a1e767001@smtp-brevo.com");
        mailSender.setPassword("1tXDbHU2QOBYE9Vv");

        // 配置JavaMail属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true"); // 生产环境改为false

        return mailSender;
    }
}