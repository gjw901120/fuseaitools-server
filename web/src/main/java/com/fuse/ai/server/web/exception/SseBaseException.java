package com.fuse.ai.server.web.exception;

import com.fuse.ai.server.web.controller.ChatController;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ErrorType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * SSE专用异常类
 * // 方式1：使用构造函数
 * new SseBaseException(UserErrorType.USER_CLIENT_ERROR, "Conversation not exist", callback);
 * // 方式2：使用静态便捷方法
 * SseBaseException.throwError(UserErrorType.USER_CLIENT_ERROR, "Conversation not exist", callback);
 * // 方式3：非中断模式
 * new SseBaseException(UserErrorType.USER_CLIENT_ERROR, "Warning", callback, "req-id", false);
 * 
 * @since 1.0
 */
@Slf4j
public class SseBaseException extends BaseException {
    
    private final ChatController.SseCallback callback;
    private final String requestId;
    private final boolean interruptExecution;

    /**
     * 构造函数，创建SSE异常并立即发送SSE事件
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     */
    public SseBaseException(ErrorType errorType, String message, ChatController.SseCallback callback) {
        this(errorType, message, callback, "sse-exception", true);
    }

    /**
     * 构造函数，创建SSE异常并立即发送SSE事件
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     * @param requestId 请求ID，用于日志追踪
     */
    public SseBaseException(ErrorType errorType, String message, ChatController.SseCallback callback, String requestId) {
        this(errorType, message, callback, requestId, true);
    }
    
    /**
     * 构造函数，创建SSE异常并立即发送SSE事件
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     * @param requestId 请求ID，用于日志追踪
     * @param interruptExecution 是否中断当前执行流程
     */
    public SseBaseException(ErrorType errorType, String message, ChatController.SseCallback callback, String requestId, boolean interruptExecution) {
        super(errorType, message);
        this.callback = callback;
        this.requestId = requestId;
        this.interruptExecution = interruptExecution;
        
        // 构造时立即发送SSE事件，不抛出异常
        sendAsSseEvent();
        
        // 如果需要中断执行，则抛出运行时异常
        if (interruptExecution) {
            throw new SseBaseExceptionInterrupt(getMessage());
        }
    }

    /**
     * 发送SSE错误事件而不抛出异常
     */
    public void sendAsSseEvent() {
        if (callback != null) {
            try {
                log.debug("{} 发送SSE业务错误事件: {}", requestId != null ? requestId : "sse-exception", getMessage());
                
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("errorMessage", getMessage());
                errorData.put("errorCode", getErrorType().getCode());
                errorData.put("type", "business_error");
                errorData.put("timestamp", System.currentTimeMillis());
                
                callback.onData(errorData);
            } catch (Exception e) {
                log.error("发送SSE错误事件失败", e);
            }
        } else {
            log.warn("SseBaseException没有提供callback，无法发送SSE事件: {}", getMessage());
        }
    }
    
    /**
     * 获取是否应该中断执行
     */
    public boolean shouldInterruptExecution() {
        return interruptExecution;
    }
    
    /**
     * 重写getMessage方法，提供默认值
     */
    @Override
    public String getMessage() {
        String message = super.getMessage();
        return message != null ? message : "Unknown error";
    }
    
    /**
     * 静态便捷方法：快速创建并抛出SSE异常
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     */
    public static void throwError(ErrorType errorType, String message, ChatController.SseCallback callback) {
        new SseBaseException(errorType, message, callback);
    }
    
    /**
     * 静态便捷方法：快速创建并抛出SSE异常
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     * @param requestId 请求ID，用于日志追踪
     */
    public static void throwError(ErrorType errorType, String message, ChatController.SseCallback callback, String requestId) {
        new SseBaseException(errorType, message, callback, requestId);
    }
    
    /**
     * 静态便捷方法：快速创建SSE异常（可选择是否中断执行）
     * 
     * @param errorType 错误类型
     * @param message 错误消息
     * @param callback SSE回调，用于发送事件
     * @param requestId 请求ID，用于日志追踪
     * @param interruptExecution 是否中断当前执行流程
     */
    public static void throwError(ErrorType errorType, String message, ChatController.SseCallback callback, String requestId, boolean interruptExecution) {
        new SseBaseException(errorType, message, callback, requestId, interruptExecution);
    }
    
    /**
     * 内部类：用于中断执行流程的运行时异常
     */
    private static class SseBaseExceptionInterrupt extends RuntimeException {
        public SseBaseExceptionInterrupt(String message) {
            super(message);
        }
        
        @Override
        public synchronized Throwable fillInStackTrace() {
            // 不填充堆栈跟踪以提高性能
            return this;
        }
    }
}