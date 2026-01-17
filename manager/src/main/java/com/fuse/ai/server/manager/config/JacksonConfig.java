package com.fuse.ai.server.manager.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary  // 标记为主要 Bean，避免冲突
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 关键配置：驼峰转下划线
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 其他常用配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }
}
