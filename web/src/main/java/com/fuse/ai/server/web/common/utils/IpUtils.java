package com.fuse.ai.server.web.common.utils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getClientIp(HttpServletRequest request) {
        // 1. 获取 X-Forwarded-For
        String ip = request.getHeader("X-Forwarded-For");
        // 如果该头存在且不为空，取第一个 IP（因为格式是 client, proxy1, proxy2）
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多个代理时，取第一个才是真实客户端 IP
            int index = ip.indexOf(",");
            return index != -1 ? ip.substring(0, index).trim() : ip.trim();
        }

        // 2. 获取 X-Real-IP（Nginx 常用）
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 3. 其他代理头（较少见）
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 4. 兜底：直接获取
        return request.getRemoteAddr();
    }
}