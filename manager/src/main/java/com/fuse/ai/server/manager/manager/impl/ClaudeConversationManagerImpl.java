package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.ClaudeFeignClient;
import com.fuse.ai.server.manager.manager.ClaudeConversationManager;
import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import com.fuse.ai.server.manager.model.response.ClaudeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClaudeConversationManagerImpl implements ClaudeConversationManager {

    private final ClaudeFeignClient claudeFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void streamChat(ClaudeRequest request, String apiKey, ChatResponseCallback<Object> callback) {
        log.info("调用Claude API，模型: {}", request.getModel());

        claudeFeignClient.streamCompletion(request, apiKey, new ClaudeFeignClient.StreamCallback() {
            @Override
            public void onData(String jsonLine) {
                try {
                    // Claude使用多态反序列化
                    Object event = objectMapper.readValue(jsonLine, ClaudeResponse.class);
                    callback.onData(event);
                } catch (Exception e) {
                    log.error("解析Claude响应失败: {}", jsonLine, e);
                    callback.onError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("Claude API调用失败", error);
                callback.onError(error);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }
}