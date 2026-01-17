package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.ChatgptFeignClient;
import com.fuse.ai.server.manager.manager.ChatgptConversationManager;
import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import com.fuse.ai.server.manager.model.response.ChatgptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatgptConversationManagerImpl implements ChatgptConversationManager {

    private final ChatgptFeignClient chatgptFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void streamChat(ChatgptRequest request, String apiKey, ChatResponseCallback<ChatgptResponse> callback) {
        log.info("调用ChatGPT API，模型: {}", request.getModel());

        chatgptFeignClient.streamCompletion(request, apiKey, new ChatgptFeignClient.StreamCallback() {
            @Override
            public void onData(String jsonLine) {
                try {
                    ChatgptResponse response = objectMapper.readValue(jsonLine, ChatgptResponse.class);
                    log.info("ChatGPT API返回结果: {}", response);
                    callback.onData(response);
                } catch (Exception e) {
                    log.error("解析ChatGPT响应失败", e);
                    callback.onError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("ChatGPT API调用失败", error);
                callback.onError(error);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }
}