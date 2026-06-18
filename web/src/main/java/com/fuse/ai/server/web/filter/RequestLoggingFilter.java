package com.fuse.ai.server.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.web.common.utils.SqlContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求日志过滤器 - 统一记录请求/响应完整信息
 * 格式：请求开始块（URI/Header）→ 执行 → 请求结束块（状态/耗时/SQL/Body/Response）
 */
@Component
@Order(2)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger REQUEST_LOG = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String START_LINE = "\n======================================== 请求开始 ========================================\n";
    private static final String END_LINE   = "\n======================================== 请求结束 ==========================================";
    private static final String BORDER     = "==========================================================================================";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = "-";
        }

        long startTime = System.currentTimeMillis();

        // 包装请求（缓存请求体）
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        // 包装响应（缓存响应体）
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // 记录请求开始
        logRequestStart(request, traceId);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 记录请求结束（含SQL统计、Body、Response）
            logRequestEnd(request, requestWrapper, responseWrapper, traceId, duration);

            // 将缓存的响应体写回原始响应（必须调用，否则客户端收不到响应）
            responseWrapper.copyBodyToResponse();

            // 清理ThreadLocal
            SqlContextHolder.clear();
        }
    }

    // ========================= 请求开始日志 =========================

    private void logRequestStart(HttpServletRequest request, String traceId) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String headers = collectHeadersJson(request);

        StringBuilder sb = new StringBuilder();
        sb.append(START_LINE);
        sb.append("[").append(traceId).append("] ").append(method).append(" ").append(uri).append("\n");
        sb.append("Query String: ").append(queryString != null ? queryString : "").append("\n");
        sb.append("Headers: ").append(headers);

        REQUEST_LOG.info(sb.toString());
    }

    // ========================= 请求结束日志 =========================

    private void logRequestEnd(HttpServletRequest request,
                               ContentCachingRequestWrapper requestWrapper,
                               ContentCachingResponseWrapper responseWrapper,
                               String traceId,
                               long duration) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        int status = responseWrapper.getStatus();

        // SQL 统计
        int sqlCount = SqlContextHolder.getSqlCount();
        long sqlTotalTime = SqlContextHolder.getTotalCostTime();
        List<SqlContextHolder.SqlLogEntry> sqlLogs = SqlContextHolder.getSqlLogs();

        // 请求体（从缓存读取）
        String requestBody = getRequestBody(requestWrapper);

        // 响应体
        String responseBody = getResponseBody(responseWrapper);

        StringBuilder sb = new StringBuilder();
        sb.append(END_LINE).append("\n");

        // 汇总行
        sb.append("[").append(traceId).append("] ")
                .append(method).append(" ").append(uri)
                .append(" | 状态: ").append(status)
                .append(" | 总耗时: ").append(duration).append("ms")
                .append(" | SQL数量: ").append(sqlCount)
                .append(" | SQL总耗时: ").append(sqlTotalTime).append("ms\n");

        // 请求体
        sb.append("Request Body: ").append(requestBody).append("\n");

        // 响应体
        sb.append("Response: ").append(responseBody);

        // SQL 明细
        for (int i = 0; i < sqlLogs.size(); i++) {
            SqlContextHolder.SqlLogEntry entry = sqlLogs.get(i);
            sb.append("\n  SQL[").append(i + 1).append("]: ")
                    .append("[SQL耗时] ").append(entry.getCostTime()).append("ms")
                    .append(" | 参数: ").append(entry.getParameters())
                    .append(" | SQL: ").append(entry.getSql());
        }

        sb.append("\n").append(BORDER);
        REQUEST_LOG.info(sb.toString());
    }

    // ========================= 工具方法 =========================

    /**
     * 收集所有 Header 为 JSON（脱敏 authorization）
     */
    private String collectHeadersJson(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            if ("authorization".equalsIgnoreCase(name) && value != null && value.length() > 6) {
                value = value.substring(0, 6) + "***";
            }
            headers.put(name, value);
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(headers);
        } catch (Exception e) {
            return headers.toString();
        }
    }

    /**
     * 读取已缓存的请求体
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "(empty)";
    }

    /**
     * 读取已缓存的响应体
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String charset = response.getCharacterEncoding() != null
                    ? response.getCharacterEncoding() : "UTF-8";
            try {
                return new String(content, charset);
            } catch (Exception e) {
                return new String(content, StandardCharsets.UTF_8);
            }
        }
        return "(empty)";
    }

    // ========================= 过滤规则 =========================

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // 排除静态资源
        if (uri.contains(".") && (uri.endsWith(".html") || uri.endsWith(".css") ||
                uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") ||
                uri.endsWith(".gif") || uri.endsWith(".ico") || uri.endsWith(".svg"))) {
            return true;
        }

        // 排除健康检查
        if ("/healthcheck".equals(uri)) {
            return true;
        }

        // 排除 SSE/流式请求（由 SseTraceIdFilter 处理）
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            return true;
        }
        return uri.contains("/sse") || uri.contains("/stream") || uri.contains("/events") ||
                uri.contains("/chat/chatgpt") || uri.contains("/chat/claude") ||
                uri.contains("/chat/gemini") || uri.contains("/chat/deepseek");
    }
}
