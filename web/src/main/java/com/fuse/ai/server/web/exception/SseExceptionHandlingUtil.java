package com.fuse.ai.server.web.exception;

import com.fuse.ai.server.web.controller.ChatController;
import com.fuse.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * SSE异常处理工具类
 * 提供统一的SSE流式请求异常处理机制
 */
@Slf4j
public class SseExceptionHandlingUtil {

    /**
     * 执行SSE异步任务并统一处理异常
     *
     * @param sseEmitter SSE发射器
     * @param requestId 请求ID用于日志追踪
     * @param asyncTask 异步任务
     * @param sseCallback SSE回调接口
     */
    public static void executeAsyncSseTask(SseEmitter sseEmitter, 
                                         String requestId, 
                                         Runnable asyncTask, 
                                         ChatController.SseCallback sseCallback) {
        CompletableFuture.runAsync(asyncTask)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    // 检查是否是因SseBaseException发送而引发的中断异常
                    if (isSseBaseExceptionInterrupt(throwable)) {
                        log.debug("[{}] SseBaseException已处理并中断执行，正常完成SSE连接", requestId);
                        // SseBaseException已经发送了SSE事件，正常完成连接
                        try {
                            sseEmitter.complete();
                        } catch (Exception e) {
                            log.error("[{}] 完成SSE连接失败", requestId, e);
                        }
                        return; // 直接返回，不继续处理其他异常
                    }
                    // 检查是否是BaseException但不是SseBaseExceptionInterrupt（避免重复处理）
                    else if (throwable instanceof BaseException baseException && !isSseBaseExceptionInterrupt(throwable)) {
                        log.debug("[{}] SSE流业务错误", requestId, baseException);
                        
                        // 使用SSE回调发送错误信息
                        Map<String, Object> errorData = new HashMap<>();
                        errorData.put("errorMessage", baseException.getMessage());
                        errorData.put("errorCode", baseException.getErrorType().getCode());
                        errorData.put("type", "business_error");
                        errorData.put("timestamp", System.currentTimeMillis());
                        
                        try {
                            // 通过SSE回调发送错误
                            if (sseCallback != null) {
                                sseCallback.onData(errorData); // 使用onData而不是onError，因为这是业务异常
                                sseEmitter.complete(); // 业务异常后正常完成
                            } else {
                                // 如果没有回调，则直接发送错误
                                SseEmitter.SseEventBuilder event = SseEmitter.event()
                                        .name("error")
                                        .data(errorData);
                                sseEmitter.send(event);
                                // 业务异常完成后正常结束，而不是错误结束
                                sseEmitter.complete();
                            }
                        } catch (Exception e) {
                            log.error("[{}] 发送业务错误事件失败", requestId, e);
                            try {
                                sseEmitter.complete();
                            } catch (Exception ex) {
                                log.error("[{}] 完成SSE连接失败", requestId, ex);
                            }
                        }
                    } else {
                        // 如果是系统异常，记录错误并使用completeWithError
                        log.error("[{}] SSE流系统错误", requestId, throwable);
                        try {
                            sseEmitter.completeWithError(throwable);
                        } catch (Exception e) {
                            log.error("[{}] 发送系统错误事件失败", requestId, e);
                        }
                    }
                }
            });
    }
    
    /**
     * 检查异常是否是由SseBaseException引发的中断
     */
    private static boolean isSseBaseExceptionInterrupt(Throwable throwable) {
        // 检查当前异常是否是SseBaseException内部的中断异常
        if (isInstanceOfSseBaseExceptionInterrupt(throwable)) {
            return true;
        }
        
        // 检查异常消息是否包含SseBaseException相关的中断标识（兼容旧版本）
        if (throwable instanceof RuntimeException && 
            "SseBaseException sent, interrupting execution".equals(throwable.getMessage())) {
            return true;
        }
        
        // 深度遍历异常链，检查所有可能的cause
        return checkExceptionChain(throwable);
    }
    
    /**
     * 递归检查异常链中是否包含SseBaseException中断
     */
    private static boolean checkExceptionChain(Throwable throwable) {
        Throwable current = throwable;
        
        // 使用集合跟踪已经检查过的异常，避免循环引用
        java.util.Set<Throwable> checked = new java.util.HashSet<>();
        
        while (current != null && !checked.contains(current)) {
            checked.add(current);
            
            // 检查当前异常
            if (isInstanceOfSseBaseExceptionInterrupt(current)) {
                return true;
            }
            
            // 检查当前异常的消息
            if (current instanceof RuntimeException && 
                "SseBaseException sent, interrupting execution".equals(current.getMessage())) {
                return true;
            }
            
            // 检查堆栈跟踪中是否包含SseBaseException的相关信息
            for (StackTraceElement element : current.getStackTrace()) {
                if (element.getClassName().contains("SseBaseException")) {
                    return true;
                }
            }
            
            // 移动到下一个cause
            current = current.getCause();
        }
        
        return false;
    }
    
    /**
     * 检查异常是否是SseBaseExceptionInterrupt类型的实例
     */
    private static boolean isInstanceOfSseBaseExceptionInterrupt(Throwable throwable) {
        try {
            Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
            // 直接检查异常本身是否是该类型
            if (clazz.isInstance(throwable)) {
                return true;
            }
            
            // 检查异常的类型名是否匹配（以防反射失败）
            if (throwable.getClass().getName().equals("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt")) {
                return true;
            }
            
            // 检查是否是子类
            Class<?> superClass = throwable.getClass().getSuperclass();
            while (superClass != null) {
                if (superClass.getName().equals("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt")) {
                    return true;
                }
                superClass = superClass.getSuperclass();
            }
            
            return false;
        } catch (Exception e) {
            // 如果反射检查失败，回退到简单名称检查
            return throwable.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
        }
    }


    /**
     * 在主线程中处理SSE异常，将异常转发到GlobalExceptionHandler
     */
    public static void handleSseException(SseEmitter sseEmitter, Throwable throwable, String requestId) {
        try {
            if (throwable instanceof BaseException baseException) {
                log.debug("[{}] SSE流业务错误", requestId, baseException);
                
                // 创建错误数据
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("errorMessage", baseException.getMessage());
                errorData.put("errorCode", baseException.getErrorType().getCode());
                errorData.put("type", "business_error");
                errorData.put("timestamp", System.currentTimeMillis());

                // 发送错误事件
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .name("error")
                        .data(errorData);
                sseEmitter.send(event);
                
                // 正常完成
                sseEmitter.complete();
            } else {
                log.error("[{}] SSE流系统错误", requestId, throwable);
                sseEmitter.completeWithError(throwable);
            }
        } catch (IOException e) {
            log.error("[{}] 发送SSE错误事件失败", requestId, e);
            try {
                sseEmitter.completeWithError(throwable);
            } catch (Exception ex) {
                log.error("[{}] 最终完成SSE连接失败", requestId, ex);
            }
        }
    }
}