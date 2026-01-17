package com.fuse.ai.server.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一的TraceId过滤器
 * 为所有请求生成唯一ID并传递到日志中
 * 特别注意：对于SSE请求，不包装响应，避免影响流式传输
 */
@Component
@Order(1)
@Slf4j
public class SseTraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final String HEADER_TRACE_ID = "X-Trace-Id";
    private static final String REQUEST_START_TIME = "requestStartTime";
    private static final String REQUEST_METHOD = "requestMethod";
    private static final String REQUEST_URI = "requestUri";
    private static final String CLIENT_IP = "clientIp";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 判断是否为SSE请求
        boolean isSseRequest = isSseRequest(request);

        // 生成或获取TraceId
        String traceId = getOrGenerateTraceId(request);

        // 设置MDC上下文
        setupMdcContext(request, traceId);

        // 设置响应头
        response.setHeader(HEADER_TRACE_ID, traceId);

        long startTime = System.currentTimeMillis();

        try {
            // 记录请求开始
            logRequestStart(request, traceId, isSseRequest);

            if (isSseRequest) {
                // 对于SSE请求，使用原始请求和响应，不进行包装
                filterChain.doFilter(request, response);
                // SSE请求是长连接，这里不会立即返回
            } else {
                // 对于普通请求，只包装请求（为了读取body），不包装响应
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

                filterChain.doFilter(requestWrapper, response);

                // 记录请求结束（对于普通请求）
                long duration = System.currentTimeMillis() - startTime;
                logRequestEnd(request, response, traceId, duration, false);
            }
        } catch (Exception e) {
            // 记录异常
            long duration = System.currentTimeMillis() - startTime;
            log.error("[{}] 请求处理异常: {} {} 耗时: {}ms 错误: {}",
                    traceId, request.getMethod(), request.getRequestURI(),
                    duration, e.getMessage(), e);
            throw e;
        } finally {
            // 对于SSE请求，记录连接建立时间
            if (isSseRequest) {
                long duration = System.currentTimeMillis() - startTime;
                // SSE连接建立后可能会保持很久，这里只记录连接建立的时间
                log.info("[{}] SSE连接建立完成: {} {} 耗时: {}ms",
                        traceId, request.getMethod(), request.getRequestURI(), duration);
            }

            // 清理MDC上下文
            MDC.clear();
        }
    }

    /**
     * 判断是否为SSE请求
     */
    private boolean isSseRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String acceptHeader = request.getHeader("Accept");

        // 通过Accept头判断
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return true;
        }

        // 通过URI路径判断
        boolean isSsePath = uri.contains("/sse") ||
                uri.contains("/stream") ||
                uri.contains("/events") ||
                uri.contains("/chat/chatgpt") ||
                uri.contains("/chat/claude") ||
                uri.contains("/chat/gemini") ||
                uri.contains("/chat/deepseek");

        return isSsePath;
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        // 优先从请求头获取
        String traceId = request.getHeader(HEADER_TRACE_ID);

        if (traceId == null || traceId.trim().isEmpty()) {
            // 生成新的TraceId
            traceId = generateTraceId();
        }

        return traceId;
    }

    private String generateTraceId() {
        // 格式: 时间戳(13位) + 随机数(6位)
        long timestamp = System.currentTimeMillis();
        int random = (int) ((Math.random() * 900000) + 100000); // 100000-999999
        return String.format("T%s%06d", timestamp, random);
    }

    private void setupMdcContext(HttpServletRequest request, String traceId) {
        // 设置TraceId
        MDC.put(TRACE_ID, traceId);

        // 设置请求开始时间
        MDC.put(REQUEST_START_TIME, String.valueOf(System.currentTimeMillis()));

        // 设置请求方法
        MDC.put(REQUEST_METHOD, request.getMethod());

        // 设置请求URI
        MDC.put(REQUEST_URI, request.getRequestURI());

        // 设置客户端IP
        MDC.put(CLIENT_IP, getClientIp(request));
    }

    private void logRequestStart(HttpServletRequest request, String traceId, boolean isSseRequest) {
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String clientIp = MDC.get(CLIENT_IP);

        String logType = isSseRequest ? "SSE" : "HTTP";

        if (queryString != null && !queryString.isEmpty()) {
            log.debug("[{}] {}请求开始: {} {}?{} 客户端IP: {}",
                    traceId, logType, method, uri, queryString, clientIp);
        } else {
            log.debug("[{}] {}请求开始: {} {} 客户端IP: {}",
                    traceId, logType, method, uri, clientIp);
        }
    }

    private void logRequestEnd(HttpServletRequest request,
                               HttpServletResponse response,
                               String traceId,
                               long duration,
                               boolean isSseRequest) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        String logType = isSseRequest ? "SSE" : "HTTP";

        // 判断是否为慢请求（超过3秒）
        boolean isSlow = duration > 3000;

        if (isSlow) {
            log.warn("[{}] {}慢请求结束: {} {} 状态码: {} 耗时: {}ms",
                    traceId, logType, method, uri, status, duration);
        } else {
            log.debug("[{}] {}请求结束: {} {} 状态码: {} 耗时: {}ms",
                    traceId, logType, method, uri, status, duration);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String[] ipHeaders = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : ipHeaders) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 对于X-Forwarded-For，取第一个IP
                if ("X-Forwarded-For".equals(header)) {
                    int index = ip.indexOf(',');
                    if (index != -1) {
                        ip = ip.substring(0, index);
                    }
                }
                return ip.trim();
            }
        }

        return request.getRemoteAddr();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // 排除静态资源
        boolean isStaticResource = uri.contains(".") &&
                (uri.endsWith(".html") ||
                        uri.endsWith(".css") ||
                        uri.endsWith(".js") ||
                        uri.endsWith(".png") ||
                        uri.endsWith(".jpg") ||
                        uri.endsWith(".gif") ||
                        uri.endsWith(".ico") ||
                        uri.endsWith(".svg"));

        return isStaticResource;
    }
}