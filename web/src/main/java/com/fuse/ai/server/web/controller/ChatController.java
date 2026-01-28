package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.exception.SseExceptionHandlingUtil;
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversationDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ChatConversationService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatConversationService chatConversationService;

    // ========== ChatGPT SSE Endpoint ==========
    @PostMapping(value ="/chatgpt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> gptConversation(@RequestBody @Valid ChatgptConversationDTO request,
                                                      @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("ChatGPT流式请求 {}", request);

        SseEmitter emitter = new SseEmitter(120_000L); // 120秒超时
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // 创建SSE回调实例
        SseCallback sseCallback = new SseCallback() {
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
                    // 检查是否是SseBaseExceptionInterrupt，如果是，说明已经在Service层处理过了
                    if (isSseBaseExceptionInterrupt(error)) {
                        log.debug("[{}] 忽略SseBaseExceptionInterrupt，已在Service层处理", requestId);
                        // SseBaseException已经发送了事件，这里直接完成即可
                        emitter.complete();
                        return;
                    }
                    
                    Map<String, Object> errorData = new HashMap<>();
                    // 根据异常类型使用不同的字段名保持一致性
                    if (error instanceof BaseException) {
                        errorData.put("errorMessage", error.getMessage());
                    } else {
                        errorData.put("message", error.getMessage());
                    }
                    errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
                    errorData.put("timestamp", System.currentTimeMillis());
                    // 标记是否为业务异常
                    errorData.put("isBusinessError", error instanceof BaseException);
                    errorData.put("type", error instanceof BaseException ? "business_error" : "system_error");

                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .name("error")
                            .id("error")
                            .data(errorData);

                    emitter.send(event);
                    // 对于业务异常，使用debug级别日志
                    if (error instanceof BaseException) {
                        log.debug("[{}] SSE流业务错误", requestId, error);
                        // 业务异常完成后正常结束，而不是错误结束
                        emitter.complete();
                    } else {
                        log.error("[{}] SSE流系统错误", requestId, error);
                        emitter.completeWithError(error);
                    }
                } catch (IOException e) {
                    log.error("[{}] 发送错误事件失败", requestId, e);
                    emitter.completeWithError(e);
                }
            }
            
            /**
             * 检查异常是否是SseBaseExceptionInterrupt类型
             */
            private boolean isSseBaseExceptionInterrupt(Throwable e) {
                try {
                    Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
                    return clazz.isInstance(e);
                } catch (ClassNotFoundException ex) {
                    // 如果类不存在，回退到简单名称检查
                    return e.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
                }
            }
        };
        
        // 使用统一的异常处理工具
        SseExceptionHandlingUtil.executeAsyncSseTask(emitter, requestId, () -> {
            chatConversationService.processChatgptStream(request, userJwtDT, sseCallback);
        }, sseCallback);

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

        // 创建SSE回调实例
        SseCallback sseCallback = new SseCallback() {
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
                    // 检查是否是SseBaseExceptionInterrupt，如果是，说明已经在Service层处理过了
                    if (isSseBaseExceptionInterrupt(error)) {
                        log.debug("[{}] 忽略SseBaseExceptionInterrupt，已在Service层处理", requestId);
                        // SseBaseException已经发送了事件，这里直接完成即可
                        emitter.complete();
                        return;
                    }
                    
                    Map<String, Object> errorData = new HashMap<>();
                    // 根据异常类型使用不同的字段名保持一致性
                    if (error instanceof BaseException) {
                        errorData.put("errorMessage", error.getMessage());
                    } else {
                        errorData.put("message", error.getMessage());
                    }
                    errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
                    errorData.put("type", error instanceof BaseException ? "business_error" : "system_error");

                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(errorData));
                } catch (IOException e) {
                    log.error("发送错误事件失败", e);
                } finally {
                    // 区分业务异常和系统异常的日志级别
                    if (error instanceof BaseException) {
                        log.debug("[{}] SSE流业务错误", requestId, error);
                        // 业务异常完成后正常结束，而不是错误结束
                        emitter.complete();
                    } else {
                        log.error("[{}] SSE流系统错误", requestId, error);
                        emitter.completeWithError(error);
                    }
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
            
            /**
             * 检查异常是否是SseBaseExceptionInterrupt类型
             */
            private boolean isSseBaseExceptionInterrupt(Throwable e) {
                try {
                    Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
                    return clazz.isInstance(e);
                } catch (ClassNotFoundException ex) {
                    // 如果类不存在，回退到简单名称检查
                    return e.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
                }
            }
        };
        
        // 使用统一的异常处理工具
        SseExceptionHandlingUtil.executeAsyncSseTask(emitter, requestId, () -> {
            chatConversationService.processClaudeStream(request, userJwtDT, sseCallback);
        }, sseCallback);

        return ResponseEntity.ok(emitter);
    }

    // ========== Gemini SSE Endpoint ==========
    @PostMapping(value = "/gemini", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> geminiConversation(@RequestBody @Valid GeminiConversationDTO request,
                                                       @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("Gemini流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        SseEmitter emitter = new SseEmitter(120_000L);
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // 创建SSE回调实例
        SseCallback sseCallback = new SseCallback() {
            @Override
            public void onData(Map<String, Object> data) {
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
                    // 检查是否是SseBaseExceptionInterrupt，如果是，说明已经在Service层处理过了
                    if (isSseBaseExceptionInterrupt(error)) {
                        log.debug("[{}] 忽略SseBaseExceptionInterrupt，已在Service层处理", requestId);
                        // SseBaseException已经发送了事件，这里直接完成即可
                        emitter.complete();
                        return;
                    }
                    
                    Map<String, Object> errorData = new HashMap<>();
                    // 根据异常类型使用不同的字段名保持一致性
                    if (error instanceof BaseException) {
                        errorData.put("errorMessage", error.getMessage());
                    } else {
                        errorData.put("message", error.getMessage());
                    }
                    errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
                    errorData.put("type", error instanceof BaseException ? "business_error" : "system_error");

                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(errorData));
                } catch (IOException e) {
                    log.error("发送错误事件失败", e);
                } finally {
                    // 区分业务异常和系统异常的日志级别
                    if (error instanceof BaseException) {
                        log.debug("[{}] SSE流业务错误", requestId, error);
                        // 业务异常完成后正常结束，而不是错误结束
                        emitter.complete();
                    } else {
                        log.error("[{}] SSE流系统错误", requestId, error);
                        emitter.completeWithError(error);
                    }
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
                } catch (IOException e) {
                    log.error("[{}] 发送完成事件失败", requestId, e);
                    emitter.completeWithError(e);
                }
            }
            
            /**
             * 检查异常是否是SseBaseExceptionInterrupt类型
             */
            private boolean isSseBaseExceptionInterrupt(Throwable e) {
                try {
                    Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
                    return clazz.isInstance(e);
                } catch (ClassNotFoundException ex) {
                    // 如果类不存在，回退到简单名称检查
                    return e.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
                }
            }
        };
        
        // 使用统一的异常处理工具
        SseExceptionHandlingUtil.executeAsyncSseTask(emitter, requestId, () -> {
            chatConversationService.processGeminiStream(request, userJwtDT, sseCallback);
        }, sseCallback);

        return ResponseEntity.ok(emitter);
    }

    // ========== DeepSeek SSE Endpoint ==========
    @PostMapping(value ="/deepseek", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> deepseekConversation(@RequestBody @Valid DeepseekConversationDTO request,
                                                         @AuthenticationPrincipal UserJwtDTO userJwtDT) {

        log.info("DeepSeek流式请求，模型: {}, 问题: {}", request.getModel(), request.getPrompt());

        SseEmitter emitter = new SseEmitter(120_000L);
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // 创建SSE回调实例
        SseCallback sseCallback = new SseCallback() {
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
                    // 检查是否是SseBaseExceptionInterrupt，如果是，说明已经在Service层处理过了
                    if (isSseBaseExceptionInterrupt(error)) {
                        log.debug("[{}] 忽略SseBaseExceptionInterrupt，已在Service层处理", requestId);
                        // SseBaseException已经发送了事件，这里直接完成即可
                        emitter.complete();
                        return;
                    }
                    
                    Map<String, Object> errorData = new HashMap<>();
                    // 根据异常类型使用不同的字段名保持一致性
                    if (error instanceof BaseException) {
                        errorData.put("errorMessage", error.getMessage());
                    } else {
                        errorData.put("message", error.getMessage());
                    }
                    errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
                    errorData.put("type", error instanceof BaseException ? "business_error" : "system_error");

                    emitter.send(SseEmitter.event()
                            .name("error")
                            .id(requestId)
                            .data(errorData));
                } catch (IOException e) {
                    log.error("发送错误事件失败", e);
                } finally {
                    // 区分业务异常和系统异常的日志级别
                    if (error instanceof BaseException) {
                        log.debug("[{}] SSE流业务错误", requestId, error);
                        // 业务异常完成后正常结束，而不是错误结束
                        emitter.complete();
                    } else {
                        log.error("[{}] SSE流系统错误", requestId, error);
                        emitter.completeWithError(error);
                    }
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
            
            /**
             * 检查异常是否是SseBaseExceptionInterrupt类型
             */
            private boolean isSseBaseExceptionInterrupt(Throwable e) {
                try {
                    Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
                    return clazz.isInstance(e);
                } catch (ClassNotFoundException ex) {
                    // 如果类不存在，回退到简单名称检查
                    return e.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
                }
            }
        };
        
        // 使用统一的异常处理工具
        SseExceptionHandlingUtil.executeAsyncSseTask(emitter, requestId, () -> {
            chatConversationService.processDeepseekStream(request, userJwtDT, sseCallback);
        }, sseCallback);

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