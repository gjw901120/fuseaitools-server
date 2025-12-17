package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ClaudeFeignClient {

    @Value("${claude.api.key}")
    private String apiKey;

    @Value("${claude.api.url:https://api.anthropic.com/v1}")
    private String apiUrl;

    @Value("${claude.api.version:2023-06-01}")
    private String apiVersion;

    private final WebClient webClient;

    public ClaudeFeignClient() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", apiVersion)
                .build();
    }

    /**
     * 流式调用Claude API
     */
    public Flux<String> streamCompletion(ClaudeRequest request) {
        log.info("调用Claude API，模型: {}", request.getModel());

        return webClient.post()
                .uri("/messages")
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6)) // 去掉"data: "前缀
                .filter(data -> !"[DONE]".equals(data) && !data.trim().isEmpty())
                .doOnError(error -> log.error("Claude API调用失败", error));
    }
}