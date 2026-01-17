package com.fuse.ai.server.manager.feign.config;

import com.fuse.ai.server.manager.feign.interceptor.ApiKeyAuthInterceptor;
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

}