package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class DeepseekFeignClient {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url:https://api.deepseek.com/v1}")
    private String apiUrl;

    private final WebClient webClient;

    public DeepseekFeignClient() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * 流式调用DeepSeek API
     */
    public Flux<String> streamCompletion(DeepseekRequest request) {
        log.info("调用DeepSeek API，模型: {}", request.getModel());

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6)) // 去掉"data: "前缀
                .filter(data -> !"[DONE]".equals(data))
                .doOnError(error -> log.error("DeepSeek API调用失败", error));
    }
}