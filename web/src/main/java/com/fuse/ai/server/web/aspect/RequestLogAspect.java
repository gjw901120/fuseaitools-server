package com.fuse.ai.server.web.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求日志切面 - AOP方案
 * 用于记录Controller方法的请求参数
 */
@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 定义不需要记录日志的URI列表
    private static final List<String> EXCLUDED_URIS = Arrays.asList(
            "/api/common/batch-upload",
            "/api/common/models/tree"
    );

    /**
     * 切点定义：拦截controller包下的所有方法
     */
    @Pointcut("execution(* com.fuse.ai.server.web.controller..*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes()).getRequest();

            String uri = request.getRequestURI();

            // 检查是否在排除列表中
            if (shouldSkipLogging(uri)) {
                // 直接执行原方法，不记录日志
                return joinPoint.proceed();
            }

            // 只记录POST请求
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                logRequestDetails(request, joinPoint);
            }

        } catch (Exception e) {
            log.warn("记录请求日志失败: {}", e.getMessage());
        }

        // 执行原方法
        return joinPoint.proceed();
    }

    /**
     * 判断是否应该跳过日志记录
     */
    private boolean shouldSkipLogging(String uri) {
        // 精确匹配
        if (EXCLUDED_URIS.contains(uri)) {
            return true;
        }

        // 或者使用前缀匹配（如果需要排除某个目录下的所有接口）
        // if (uri.startsWith("/api/common/")) {
        //     return true;
        // }

        return false;
    }

    private void logRequestDetails(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 收集请求Header
        Map<String, String> headers = collectHeaders(request);

        // 获取请求体
        String body = getRequestBody(request);

        // 记录日志
        log.info("AOP拦截 - 请求接口: {} {}, Header参数: {}, Body参数: {}",
                method, uri,
                formatHeaders(headers),
                body);
    }

    /**
     * 收集重要的请求头
     */
    private Map<String, String> collectHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (isImportantHeader(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }

    /**
     * 判断是否为重要Header
     */
    private boolean isImportantHeader(String headerName) {
        String lowerHeaderName = headerName.toLowerCase();
        return lowerHeaderName.startsWith("x-") ||
                "authorization".equals(lowerHeaderName) ||
                "content-type".equals(lowerHeaderName) ||
                "user-agent".equals(lowerHeaderName) ||
                "referer".equals(lowerHeaderName) ||
                lowerHeaderName.contains("token") ||
                lowerHeaderName.contains("key") ||
                lowerHeaderName.contains("id");
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request) {
        try {
            // 尝试包装请求以获取内容
            ContentCachingRequestWrapper wrappedRequest;
            if (request instanceof ContentCachingRequestWrapper) {
                wrappedRequest = (ContentCachingRequestWrapper) request;
            } else {
                wrappedRequest = new ContentCachingRequestWrapper(request);
            }

            byte[] content = wrappedRequest.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, StandardCharsets.UTF_8);
            } else if (request.getContentLength() > 0) {
                return "[请求体可能已被读取，Content-Length: " + request.getContentLength() + "]";
            }
            return "[空请求体]";
        } catch (Exception e) {
            log.warn("读取请求体失败: {}", e.getMessage());
            return "[读取请求体失败]";
        }
    }

    /**
     * 格式化Headers输出
     */
    private String formatHeaders(Map<String, String> headers) {
        if (headers.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(headers);
        } catch (Exception e) {
            return headers.toString();
        }
    }
}