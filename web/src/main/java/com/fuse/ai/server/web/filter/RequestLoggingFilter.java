package com.fuse.ai.server.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数日志记录过滤器 - 最终解决方案
 * 使用最高优先级确保在任何组件读取请求体前就包装请求
 */
@Slf4j
public class RequestLoggingFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 检查是否为需要跳过的接口
        if (shouldSkipLogging(httpRequest)) {
            // 直接放行，不记录日志
            chain.doFilter(request, response);
            return;
        }

        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) &&
                httpRequest.getRequestURI().startsWith("/api/")) {

            // 关键：立即包装请求，确保在任何其他过滤器读取前就缓存请求体
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);

            // 立即读取并记录日志，这时请求体还在缓存中
            logRequestParameters(wrappedRequest);

            // 将包装后的请求传递给过滤器链
            chain.doFilter(wrappedRequest, response);
        } else {
            // 非目标请求直接放行
            chain.doFilter(request, response);
        }
    }

    private void logRequestParameters(ContentCachingRequestWrapper request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 收集请求Header
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (isImportantHeader(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }

        // 获取请求体内容
        String body = getRequestBody(request);

        // 记录日志
        log.info("POST请求接口: {} {}, Header参数: {}, Body参数: {}",
                method, uri,
                formatHeaders(headers),
                body != null ? body : "[请求体已被其他过滤器读取]");
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

    /**
     * 判断是否应该跳过日志记录
     */
    private boolean shouldSkipLogging(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 跳过 /batch-upload 接口的日志记录
        return uri.endsWith("/batch-upload");
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            // 直接从缓存获取字节数组，不调用getReader()或getInputStream()，以免影响后续的Controller读取
            byte[] contentAsByteArray = request.getContentAsByteArray();
            
            if (contentAsByteArray != null && contentAsByteArray.length > 0) {
                String characterEncoding = request.getCharacterEncoding() != null ?
                        request.getCharacterEncoding() : "UTF-8";
                return new String(contentAsByteArray, characterEncoding);
            }
        } catch (Exception e) {
            log.warn("读取请求体失败: {}", e.getMessage());
        }
        // 如果字节数组为空但Content-Length大于0，说明请求体可能已被其他过滤器读取
        if (request.getContentLength() > 0) {
            return "[请求体已被其他过滤器读取，Content-Length: " + request.getContentLength() + "]";
        }
        return "[空请求体]";
    }
}