package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.manager.model.response.GeminiResponse;
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversionDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ChatConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatConversionService  chatConversionService;

    @PostMapping(value ="/chatgpt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> gptConversion(@RequestBody @Valid ChatgptConversionDTO request, @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("ChatGPT流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        return chatConversionService.processChatgptStream(request, userJwtDT)
                .map(response -> {
                    Map<String, Object> eventData = new HashMap<>();
                    // 根据响应类型处理
                    String eventType = response.getObject();
                    eventData.put("type", eventType);
                    // 内容增量事件
                    eventData.put("delta", response.getContent());

                    // 可以添加更多事件类型的处理

                    return ServerSentEvent.<Object>builder()
                            .data(eventData)
                            .event(eventType)
                            .id(response.getId() != null ? response.getId() : null)
                            .build();
                })
                .onErrorResume(e -> {
                    log.error("ChatGPT流式处理失败", e);
                    return Flux.just(ServerSentEvent.<Object>builder()
                            .data(Map.of("error", e.getMessage()))
                            .event("error")
                            .build());
                });
    }

    @PostMapping(value ="/claude", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> claudeConversion(@RequestBody @Valid ClaudeConversionDTO request, @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("Claude流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        return chatConversionService.processClaudeStream(request, userJwtDT)
                .map(aggregatedEvent -> {
                    Map<String, Object> eventData = new HashMap<>();

                    // 始终包含累积内容
                    eventData.put("content", aggregatedEvent.getContent());

                    // 如果是增量内容，包含delta
                    if (aggregatedEvent.getDelta() != null) {
                        eventData.put("delta", aggregatedEvent.getDelta());
                    }

                    // 是否最终块
                    eventData.put("is_final", aggregatedEvent.getIsFinal());

                    // 如果是最终块，包含使用情况和停止原因
                    if (Boolean.TRUE.equals(aggregatedEvent.getIsFinal())) {
                        if (aggregatedEvent.getUsage() != null) {
                            eventData.put("usage", aggregatedEvent.getUsage());
                        }
                        if (aggregatedEvent.getStopReason() != null) {
                            eventData.put("stop_reason", aggregatedEvent.getStopReason());
                        }
                    }

                    return ServerSentEvent.<Object>builder()
                            .data(eventData)
                            .event("message")
                            .id(UUID.randomUUID().toString())
                            .build();
                })
                .onErrorResume(e -> {
                    log.error("Claude流式处理失败", e);
                    return Flux.just(ServerSentEvent.<Object>builder()
                            .data(Map.of("error", e.getMessage()))
                            .event("error")
                            .build());
                });
    }

    @PostMapping(value = "/gemini", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> geminiConversion(@RequestBody @Valid GeminiConversionDTO request, @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("Gemini流式请求，模型: {}, 问题: {}",
                request.getModel(), request.getPrompt());

        return chatConversionService.processGeminiStream(request, userJwtDT)
                .map(response -> {
                    Map<String, Object> eventData = new HashMap<>();

                    // 处理内容增量
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        GeminiResponse.Delta delta = response.getChoices().get(0).getDelta();
                        if (delta != null) {
                            // 普通内容
                            if (delta.getContent() != null) {
                                eventData.put("content", delta.getContent());
                            }
                            // 推理内容（Gemini特有）
                            if (delta.getReasoningContent() != null) {
                                eventData.put("reasoning_content", delta.getReasoningContent());
                            }
                            if (delta.getReasoning() != null) {
                                eventData.put("reasoning", delta.getReasoning());
                            }
                        }
                    }

                    // 是否为最终块
                    eventData.put("is_final", Boolean.TRUE.equals(response.getIsFinal()));

                    // 完成原因
                    if (response.getFinishReason() != null) {
                        eventData.put("finish_reason", response.getFinishReason());
                    }

                    return ServerSentEvent.<Object>builder()
                            .data(eventData)
                            .event("message")
                            .id(response.getId() != null ? response.getId() : UUID.randomUUID().toString())
                            .build();
                })
                .onErrorResume(e -> {
                    log.error("Gemini流式处理失败", e);
                    return Flux.just(ServerSentEvent.<Object>builder()
                            .data(Map.of("error", e.getMessage()))
                            .event("error")
                            .build());
                });
    }

    @PostMapping(value ="/deepseek", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> deepseekConversion(@RequestBody @Valid DeepseekConversionDTO request, @AuthenticationPrincipal UserJwtDTO userJwtDT) {
        log.info("DeepSeek流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        return chatConversionService.processDeepseekStream(request, userJwtDT)
                .map(response -> {
                    Map<String, Object> eventData = new HashMap<>();

                    // 添加增量内容
                    if (response.getChoices() != null && !response.getChoices().isEmpty() &&
                            response.getChoices().get(0).getDelta() != null) {
                        String content = response.getChoices().get(0).getDelta().getContent();
                        eventData.put("content", content);
                    }

                    // 如果是最终块，添加统计信息
                    if (Boolean.TRUE.equals(response.getIsFinal())) {
                        eventData.put("is_final", true);
                        eventData.put("finish_reason", response.getFinishReason());

                        if (response.getUsage() != null) {
                            eventData.put("usage", response.getUsage());
                        }

                        if (response.getSearchResults() != null) {
                            eventData.put("search_results", response.getSearchResults());
                        }
                    } else {
                        eventData.put("is_final", false);
                    }

                    return ServerSentEvent.<Object>builder()
                            .data(eventData)
                            .event("message")
                            .id(response.getId())
                            .build();
                })
                .onErrorResume(e -> {
                    log.error("流式处理失败", e);
                    return Flux.just(ServerSentEvent.<Object>builder()
                            .data(Map.of("error", e.getMessage()))
                            .event("error")
                            .build());
                });
    }

}
