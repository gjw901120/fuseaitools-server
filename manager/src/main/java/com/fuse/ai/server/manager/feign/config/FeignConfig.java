package com.fuse.ai.server.manager.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor videoRequestInterceptor() {
        return template -> {
            template.header("Content-Type", "application/json");
            // 添加认证头等其他配置
        };
    }
}