package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.GeminiFeignClient;
import com.fuse.ai.server.manager.manager.GeminiConversionManager;
import com.fuse.ai.server.manager.model.request.GeminiRequest;
import com.fuse.ai.server.manager.model.response.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiConversionManagerImpl implements GeminiConversionManager {

    private final GeminiFeignClient geminiFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flux<GeminiResponse> streamChat(GeminiRequest request) {
        return geminiFeignClient.streamCompletion(request)
                .handle((jsonLine, sink) -> {
                    try {
                        GeminiResponse response = objectMapper.readValue(jsonLine, GeminiResponse.class);

                        // 检查是否为最终块（usage不为空）
                        if (response.getUsage() != null) {
                            response.setIsFinal(true);
                        }

                        // 检查是否包含finish_reason
                        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                            String finishReason = response.getChoices().get(0).getFinishReason();
                            if (finishReason != null && !finishReason.isEmpty()) {
                                response.setFinishReason(finishReason);
                                response.setIsFinal(true);
                            }
                        }

                        sink.next(response);
                        log.debug("解析Gemini响应成功，是否最终: {}", response.getIsFinal());

                    } catch (Exception e) {
                        log.error("解析Gemini响应失败，原始数据: {}", jsonLine, e);
                        sink.error(new RuntimeException("解析Gemini响应失败: " + e.getMessage()));
                    }
                });
    }
}