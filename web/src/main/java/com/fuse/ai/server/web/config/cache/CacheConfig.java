package com.fuse.ai.server.web.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("cache-config")
@Data
public class CacheConfig {
    private String appPrefix = "app";
    private Map<String, CacheSpec> redis;

    @Data
    public static class CacheSpec {
        private Long expireTime = 3600L; // 默认1小时
        private Long maxSize;
    }
}