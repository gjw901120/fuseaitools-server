package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.GeminiRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class GeminiFeignClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://www.towerfun.net/v1}")
    private String apiUrl;

    private final WebClient webClient;

    public GeminiFeignClient() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    /**
     * 流式调用Gemini API
     */
    public Flux<String> streamCompletion(GeminiRequest request) {
        log.info("调用Gemini API，模型: {}", request);


        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6)) // 去掉"data: "前缀
                .filter(data -> !"[DONE]".equals(data.trim()))
                .doOnNext(data -> log.debug("收到Gemini原始响应: {}", data))
                .doOnError(error -> log.error("Gemini API调用失败", error));
    }
}