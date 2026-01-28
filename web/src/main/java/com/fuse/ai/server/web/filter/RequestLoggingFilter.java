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

        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) &&
                httpRequest.getRequestURI().startsWith("/api/")) {

            // 包装请求以缓存内容，但如果已经是ContentCachingRequestWrapper则不需要重复包装
            ContentCachingRequestWrapper wrappedRequest;
            if (httpRequest instanceof ContentCachingRequestWrapper) {
                wrappedRequest = (ContentCachingRequestWrapper) httpRequest;
            } else {
                wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
            }

            // 记录请求参数
            logRequestParameters(wrappedRequest);

            // 继续执行过滤链
            chain.doFilter(wrappedRequest, response);
        } else {
            // 非目标请求直接放行
            chain.doFilter(request, response);
        }
    }

    private void logRequestParameters(ContentCachingRequestWrapper request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 分别收集header、query和body参数
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queries = new HashMap<>();
        String body = null;

        // 收集请求Header
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // 记录重要的认证和内容相关头部
            if (isImportantHeader(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }

        // 收集查询参数
        String queryString = request.getQueryString();
        if (queryString != null) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queries.put(decodeUrl(keyValue[0]), decodeUrl(keyValue[1]));
                } else if (keyValue.length == 1) {
                    queries.put(decodeUrl(keyValue[0]), "");
                }
            }
        }

        // 确保请求体内容被缓存，然后再获取请求体参数
        body = getRequestBody(request);

        // 格式化并记录日志
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("POST请求接口: ").append(method).append(" ").append(uri);

        if (!headers.isEmpty()) {
            logMessage.append(", Header参数: ").append(formatHeaders(headers));
        }

        if (!queries.isEmpty()) {
            logMessage.append(", Query参数: ").append(formatParams(queries));
        }

        // 记录body信息，特别是针对已知有body的情况
        if (body != null) {
            if (!body.trim().isEmpty() && !body.equals("null")) {
                logMessage.append(", Body参数: ").append(body);
            } else if (body.equals("null")) {
                logMessage.append(", Body参数: \"null\"");
            } else {
                logMessage.append(", Body参数: \"\"");
            }
        } else {
            // 记录详细信息，便于调试
            int contentLength = request.getContentLength();
            String contentType = request.getContentType();
            logMessage.append(", Body参数: null (Content-Type: ").append(contentType)
                    .append(", Content-Length: ").append(contentLength).append(")");
        }

        log.info(logMessage.toString());
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
     * 格式化参数输出
     */
    private String formatParams(Map<String, String> params) {
        if (params.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(params);
        } catch (Exception e) {
            return params.toString();
        }
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            // 确保内容已经被缓存
            // 首先尝试直接从缓存获取
            byte[] contentAsByteArray = request.getContentAsByteArray();
            
            // 如果缓存为空，但Content-Length大于0，说明请求体存在但尚未被缓存
            if ((contentAsByteArray == null || contentAsByteArray.length == 0) && request.getContentLength() > 0) {
                // 尝试通过getReader()强制读取内容以触发缓存
                try (java.io.BufferedReader reader = request.getReader()) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    // 读取后，内容应该已经被缓存
                    contentAsByteArray = request.getContentAsByteArray();
                } catch (Exception e) {
                    log.warn("通过getReader()读取请求体时失败: {}", e.getMessage());
                }
            }
            
            if (contentAsByteArray != null && contentAsByteArray.length > 0) {
                String characterEncoding = request.getCharacterEncoding() != null ?
                        request.getCharacterEncoding() : "UTF-8";
                return new String(contentAsByteArray, characterEncoding);
            }
        } catch (Exception e) {
            log.warn("读取请求体失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * URL解码
     */
    private String decodeUrl(String encodedUrl) {
        try {
            return java.net.URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (Exception e) {
            return encodedUrl;
        }
    }
}