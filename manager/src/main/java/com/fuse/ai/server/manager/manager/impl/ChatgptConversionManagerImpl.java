package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.ChatgptFeignClient;
import com.fuse.ai.server.manager.manager.ChatgptConversionManager;
import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import com.fuse.ai.server.manager.model.response.ChatgptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatgptConversionManagerImpl implements ChatgptConversionManager {

    private final ChatgptFeignClient chatgptFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flux<ChatgptResponse> streamChat(ChatgptRequest request) {
        return chatgptFeignClient.streamCompletion(request)
                .handle((jsonLine, sink) -> {
                    try {
                        ChatgptResponse response = objectMapper.readValue(jsonLine, ChatgptResponse.class);
                        sink.next(response);
                    } catch (Exception e) {
                        log.error("解析ChatGPT响应失败", e);
                        sink.error(e);
                    }
                });
    }
}