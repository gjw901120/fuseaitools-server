package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.GeminiFeignClient;
import com.fuse.ai.server.manager.manager.GeminiConversationManager;
import com.fuse.ai.server.manager.model.request.GeminiRequest;
import com.fuse.ai.server.manager.model.response.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiConversationManagerImpl implements GeminiConversationManager {

    private final GeminiFeignClient geminiFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void streamChat(GeminiRequest request, String apiKey, ChatResponseCallback<GeminiResponse> callback) {
        log.info("调用Gemini API，模型: {}", request.getModel());

        geminiFeignClient.streamCompletion(request, apiKey, new GeminiFeignClient.StreamCallback() {
            @Override
            public void onData(String jsonLine) {
                try {
                    GeminiResponse response = objectMapper.readValue(jsonLine, GeminiResponse.class);

                    callback.onData(response);
                } catch (Exception e) {
                    log.error("解析Gemini响应失败，原始数据: {}", jsonLine, e);
                    callback.onError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("Gemini API调用失败", error);
                callback.onError(error);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }
}