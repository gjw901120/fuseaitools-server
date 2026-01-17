package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.chat.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ChatConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatConversationService chatConversationService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    // ========== ChatGPT SSE Endpoint ==========
    @PostMapping(value ="/chatgpt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> gptConversation(@RequestBody @Valid ChatgptConversationDTO request,
                                                      @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("ChatGPT流式请求 {}", request);

        SseEmitter emitter = new SseEmitter(120_000L); // 120秒超时
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        executor.execute(() -> {
            try {
                chatConversationService.processChatgptStream(request, userJwtDT, new SseCallback() {
                    private int eventCounter = 0;

                    @Override
                    public void onData(Map<String, Object> data) {
                        try {
                            int eventId = ++eventCounter;
                            data.put("eventId", eventId);

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("message")
                                    .id(requestId)
                                    .data(data);

                            emitter.send(event);
                            log.debug("[{}] 发送消息事件 {}: {}", requestId, eventId, data.get("content"));
                        } catch (IOException e) {
                            log.error("[{}] 发送SSE事件失败", requestId, e);
                            throw new RuntimeException("SSE发送失败", e);
                        }
                    }

                    @Override
                    public void onEnd(Map<String, Object> endData) {
                        try {
                            endData.put("status", "completed");
                            endData.put("eventId", eventCounter);
                            endData.put("content", "");
                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("end")
                                    .id(requestId)
                                    .data(endData);

                            emitter.send(event);
                            log.info("[{}] 发送结束事件", requestId);
                        } catch (IOException e) {
                            log.error("[{}] 发送结束事件失败", requestId, e);
                        }
                    }

                    @Override
                    public void onComplete(Map<String, Object> completeData) {
                        try {
                            completeData.put("status", "completed");
                            completeData.put("content", "");

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("complete")
                                    .id(requestId)
                                    .data(completeData);

                            emitter.send(event);
                            emitter.complete();
                            log.info("[{}] SSE流完成，共发送{}个事件", requestId, eventCounter);
                        } catch (IOException e) {
                            log.error("[{}] 发送完成事件失败", requestId, e);
                            emitter.completeWithError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            Map<String, Object> errorData = new HashMap<>();
                            errorData.put("error", error.getMessage());
                            errorData.put("timestamp", System.currentTimeMillis());

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("error")
                                    .id("error")
                                    .data(errorData);

                            emitter.send(event);
                            log.error("[{}] SSE流错误", requestId, error);
                        } catch (IOException e) {
                            log.error("[{}] 发送错误事件失败", requestId, e);
                        } finally {
                            emitter.completeWithError(error);
                        }
                    }
                });
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        // 设置SSE完成和错误处理
        emitter.onCompletion(() -> log.info("[{}] SSE连接完成", requestId));
        emitter.onTimeout(() -> log.warn("[{}] SSE连接超时", requestId));
        emitter.onError(e -> log.error("[{}] SSE连接错误", requestId, e));

        return ResponseEntity.ok(emitter);
    }

    // ========== Claude SSE Endpoint ==========
    @PostMapping(value ="/claude", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> claudeConversation(@RequestBody @Valid ClaudeConversationDTO request,
                                                       @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("Claude流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        SseEmitter emitter = new SseEmitter(120_000L);
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        executor.execute(() -> {
            try {
                chatConversationService.processClaudeStream(request, userJwtDT, new SseCallback() {

                    private int eventCounter = 0;
                    @Override
                    public void onData(Map<String, Object> data) {

                        int eventId = ++eventCounter;
                        data.put("eventId", eventId);

                        try {
                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("message")
                                    .id(requestId)
                                    .data(data);

                            emitter.send(event);
                        } catch (IOException e) {
                            throw new RuntimeException("SSE发送失败", e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            Map<String, Object> errorData = new HashMap<>();
                            errorData.put("error", error.getMessage());

                            emitter.send(SseEmitter.event()
                                    .name("error")
                                    .data(errorData));
                        } catch (IOException e) {
                            log.error("发送错误事件失败", e);
                        } finally {
                            emitter.completeWithError(error);
                        }
                    }

                    @Override
                    public void onComplete(Map<String, Object> completeData) {
                        try {
                            completeData.put("status", "completed");
                            completeData.put("content", "");

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("complete")
                                    .id(requestId)
                                    .data(completeData);

                            emitter.send(event);
                            emitter.complete();
                            log.info("[{}] SSE流完成，共发送{}个事件", requestId, eventCounter);
                        } catch (IOException e) {
                            log.error("[{}] 发送完成事件失败", requestId, e);
                            emitter.completeWithError(e);
                        }
                    }
                });
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

    // ========== Gemini SSE Endpoint ==========
    @PostMapping(value = "/gemini", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> geminiConversation(@RequestBody @Valid GeminiConversationDTO request,
                                                       @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("Gemini流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        SseEmitter emitter = new SseEmitter(120_000L);
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        executor.execute(() -> {
            try {
                chatConversationService.processGeminiStream(request, userJwtDT, new SseCallback() {

                    private int eventCounter = 0;

                    @Override
                    public void onData(Map<String, Object> data) {

                        Integer eventId = eventCounter++;

                        try {

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("message")
                                    .id(requestId)
                                    .data(data);

                            emitter.send(event);
                        } catch (IOException e) {
                            throw new RuntimeException("SSE发送失败", e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            Map<String, Object> errorData = new HashMap<>();
                            errorData.put("error", error.getMessage());

                            emitter.send(SseEmitter.event()
                                    .name("error")
                                    .data(errorData));
                        } catch (IOException e) {
                            log.error("发送错误事件失败", e);
                        } finally {
                            emitter.completeWithError(error);
                        }
                    }

                    @Override
                    public void onComplete(Map<String, Object> completeData) {
                        try {
                            completeData.put("status", "completed");
                            completeData.put("content", "");

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("complete")
                                    .id(requestId)
                                    .data(completeData);

                            emitter.send(event);
                            emitter.complete();
                            log.info("[{}] SSE流完成，共发送{}个事件", requestId, eventCounter);
                        } catch (IOException e) {
                            log.error("[{}] 发送完成事件失败", requestId, e);
                            emitter.completeWithError(e);
                        }
                    }
                });
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

    // ========== DeepSeek SSE Endpoint ==========
    @PostMapping(value ="/deepseek", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> deepseekConversation(@RequestBody @Valid DeepseekConversationDTO request,
                                                         @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("DeepSeek流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        SseEmitter emitter = new SseEmitter(120_000L);
        String requestId = UUID.randomUUID().toString().substring(0, 8);


        executor.execute(() -> {
            try {
                chatConversationService.processDeepseekStream(request, userJwtDT, new SseCallback() {

                    private int eventCounter = 0;

                    @Override
                    public void onData(Map<String, Object> data) {

                        Integer eventId = eventCounter++;

                        try {
                            data.put("eventId", eventId);

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("message")
                                    .id(requestId)
                                    .data(data);

                            emitter.send(event);
                        } catch (IOException e) {
                            throw new RuntimeException("SSE发送失败", e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            Map<String, Object> errorData = new HashMap<>();
                            errorData.put("error", error.getMessage());

                            emitter.send(SseEmitter.event()
                                    .name("error")
                                    .id(requestId)
                                    .data(errorData));
                        } catch (IOException e) {
                            log.error("发送错误事件失败", e);
                        } finally {
                            emitter.completeWithError(error);
                        }
                    }

                    @Override
                    public void onComplete(Map<String, Object> completeData) {

                        try {
                            completeData.put("status", "completed");
                            completeData.put("content", "");

                            SseEmitter.SseEventBuilder event = SseEmitter.event()
                                    .name("complete")
                                    .id(requestId)
                                    .data(completeData);

                            emitter.send(event);
                            emitter.complete();
                            log.info("[{}] SSE流完成，共发送{}个事件", requestId, eventCounter);
                        } catch (IOException e) {
                            log.error("[{}] 发送完成事件失败", requestId, e);
                            emitter.completeWithError(e);
                        }

                    }
                });
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

    // SSE回调接口
    public interface SseCallback {
        default void onData(Map<String, Object> data) {}
        default void onEnd(Map<String, Object> endData) {}
        default void onComplete(Map<String, Object> data) {}
        default void onError(Throwable error) {}
    }
}