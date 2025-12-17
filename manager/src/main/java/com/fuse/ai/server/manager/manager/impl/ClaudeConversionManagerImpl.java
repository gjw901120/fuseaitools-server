package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.ClaudeFeignClient;
import com.fuse.ai.server.manager.manager.ClaudeConversionManager;
import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import com.fuse.ai.server.manager.model.response.ClaudeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClaudeConversionManagerImpl implements ClaudeConversionManager {

    private final ClaudeFeignClient claudeFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<Object> streamChat(ClaudeRequest request) {
        return claudeFeignClient.streamCompletion(request)
                .handle((jsonLine, sink) -> {
                    try {
                        // 使用ObjectMapper的多态反序列化
                        ClaudeResponse event = objectMapper.readValue(jsonLine, ClaudeResponse.class);
                        sink.next(event);
                    } catch (Exception e) {
                        log.error("解析Claude响应失败: {}", jsonLine, e);
                        sink.error(e);
                    }
                })
                .doOnError(error -> log.error("Claude流式处理失败", error));
    }
}