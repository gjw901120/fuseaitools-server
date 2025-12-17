package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ChatgptFeignClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1}")
    private String apiUrl;

    private final WebClient webClient;

    public ChatgptFeignClient() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * 流式调用ChatGPT API（响应式API）
     */
    public Flux<String> streamCompletion(ChatgptRequest request) {
        log.info("调用ChatGPT API，模型: {}", request.getModel());

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6)) // 去掉"data: "前缀
                .filter(data -> !"[DONE]".equals(data))
                .doOnError(error -> log.error("ChatGPT API调用失败", error));
    }
}