package com.fuse.ai.server.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("text:event-stream", MediaType.TEXT_EVENT_STREAM);
        configurer.favorPathExtension(false);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 确保MappingJackson2HttpMessageConverter支持SSE
        boolean hasJsonConverter = converters.stream()
                .anyMatch(c -> c instanceof MappingJackson2HttpMessageConverter);
        if (!hasJsonConverter) {
            converters.add(new MappingJackson2HttpMessageConverter());
        }
    }
}