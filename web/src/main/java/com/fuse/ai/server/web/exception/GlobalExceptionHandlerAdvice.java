package com.fuse.ai.server.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandlerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        // 检查是否是SseBaseExceptionInterrupt，如果是则跳过处理（因为SSE事件已发送）
        if (isSseBaseExceptionInterrupt(e)) {
            log.debug("全局异常处理器跳过SseBaseExceptionInterrupt异常: {}", e.getMessage());
            // 不做任何处理，因为SSE事件应该已经发送了
            // 注意：如果响应已经被标记为提交，则不能写入任何内容
            try {
                if (response.isCommitted()) {
                    // 响应已经提交，无法写入任何内容
                    return;
                }
            } catch (Exception ex) {
                // 如果检查响应状态失败，也安全返回
                return;
            }
            return;
        }

        // 区分业务异常和系统异常，只对系统异常记录错误日志
        if (e instanceof BaseException) {
            // 对于业务异常，仅记录调试信息，不输出完整的异常堆栈
            log.debug("全局异常处理器捕获业务异常: {}", e.getMessage());
        } else {
            FeishuMessageUtil.sendExceptionMessage(e.toString());
            // 对于系统异常，记录错误日志及完整堆栈
            log.error("全局异常处理器捕获系统异常: ", e);
        }

        // 判断是否为SSE流式请求
        String acceptHeader = request.getHeader("Accept");
        boolean isSseRequest = acceptHeader != null &&
                acceptHeader.contains(MediaType.TEXT_EVENT_STREAM_VALUE);

        // 判断是否为WebSocket请求
        String upgradeHeader = request.getHeader("Upgrade");
        boolean isWebSocketRequest = "websocket".equalsIgnoreCase(upgradeHeader);

        if (isSseRequest && !isWebSocketRequest) {
            // SSE流式请求 - 返回SSE格式的错误
            handleSseError(e, request, response);
        } else {
            // 普通HTTP请求 - 返回标准JSON错误格式
            handleJsonError(e, request, response);
        }
    }
    
    /**
     * 检查异常是否是SseBaseExceptionInterrupt类型
     */
    private boolean isSseBaseExceptionInterrupt(Exception e) {
        try {
            Class<?> clazz = Class.forName("com.fuse.ai.server.web.exception.SseBaseException$SseBaseExceptionInterrupt");
            return clazz.isInstance(e);
        } catch (ClassNotFoundException ex) {
            // 如果类不存在，回退到简单名称检查
            return e.getClass().getSimpleName().contains("SseBaseExceptionInterrupt");
        }
    }

    /**
     * 处理SSE流式请求的错误
     */
    private void handleSseError(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        // 如果是SseBaseExceptionInterrupt，不应该到达这里，因为应该已被跳过
        if (isSseBaseExceptionInterrupt(e)) {
            log.debug("SSE错误处理器不应处理SseBaseExceptionInterrupt: {}", e.getMessage());
            return; // 安全返回
        }
        
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK); // SSE保持200状态码

        Map<String, Object> errorData = new LinkedHashMap<>();
        errorData.put("errorMessage", e.getMessage());
        
        // 根据异常类型设置不同的错误码
        if (e instanceof BaseException baseEx) {
            errorData.put("errorCode", baseEx.getErrorType().getCode());
            errorData.put("type", "business_error");
        } else {
            errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
            errorData.put("type", "system_error");
        }

        PrintWriter writer = response.getWriter();
        writer.write("event: error\n");
        writer.write("data: " + objectMapper.writeValueAsString(errorData) + "\n\n");
        writer.flush();
    }

    /**
     * 处理普通HTTP请求的错误
     */
    private void handleJsonError(Exception e,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorMessage", e.getMessage());
        
        // 根据异常类型设置不同的错误码
        if (e instanceof BaseException baseEx) {
            body.put("errorCode", baseEx.getErrorType().getCode());
            body.put("type", "business_error");
        } else {
            body.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
            body.put("type", "system_error");
        }
        
        body.put("data", new Object());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}