package com.fuse.ai.server.inner.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("cache-config")
@Data
public class MyCacheConfig {
    public static final String USER = "user";
    public static final String VERIFYCODE = "verifycode";
    public static final String VERIFYCODE_LIMIT = "verifycodeLimit";

    private String appPrefix;
    private Map<String, CacheSpec> redis;
    private Map<String, CacheSpec> caffeine;

    @Data
    public static class CacheSpec {
        private Long expireTime;
        private Long maxSize;
    }
}
