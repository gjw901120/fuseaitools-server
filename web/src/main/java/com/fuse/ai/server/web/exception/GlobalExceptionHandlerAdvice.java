package com.fuse.ai.server.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.common.core.entity.vo.ResponseResult;
import com.fuse.common.core.exception.error.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        log.error("全局异常处理器捕获异常: ", e);

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
     * 处理SSE流式请求的错误
     */
    private void handleSseError(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK); // SSE保持200状态码

        Map<String, Object> errorData = new LinkedHashMap<>();
        errorData.put("errorMessage", e.getMessage());
        errorData.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());

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
        body.put("errorCode", SystemErrorType.SYSTEM_EXECUTION_ERROR.getCode());
        body.put("data", new Object());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}