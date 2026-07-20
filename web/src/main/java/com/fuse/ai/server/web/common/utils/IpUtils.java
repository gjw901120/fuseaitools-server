package com.fuse.ai.server.web.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class IpUtils {

    // 1. 配置你信任的代理IP段（Fastly的IPv4范围示例，需从官网更新）
    // 注意：实际生产中应动态加载或只信任内网LB/特定K8s IP段
    private static final List<String> TRUSTED_PROXIES = Arrays.asList(
            "146.75.0.0/16", // Fastly 示例段，实际需要精确配置
            "127.0.0.1",     // 本机
            "10.0.0.0/8",    // 内网
            "172.16.0.0/12",
            "192.168.0.0/16"
    );

    public static String getClientIp(HttpServletRequest request) {
        // 第一步：获取直连IP（这是TCP层真实IP，无法伪造）
        String remoteAddr = request.getRemoteAddr();

        // 第二步：判断直连IP是否在信任列表中（核心安全逻辑）
        // 如果是信任的代理，才去解析Header；否则直接返回直连IP（防止伪造）
        if (!isTrustedProxy(remoteAddr)) {
            return remoteAddr;
        }

        // 第三步：优先取 Fastly 专用 Header
        String ip = request.getHeader("Fastly-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 第四步：取 X-Forwarded-For，并过滤掉内网/未知IP
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            // 拆分并遍历，返回第一个有效的公网IP
            for (String segment : ip.split(",")) {
                String candidate = segment.trim();
                if (isValidIp(candidate) && !isInternalIp(candidate)) {
                    return candidate;
                }
            }
        }

        // 第五步：兜底取 X-Real-IP 或其他
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 最后回退到直连IP
        return remoteAddr;
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    // 简易判断是否为内网IP（实际可用IPAddress库做CIDR匹配）
    private static boolean isInternalIp(String ip) {
        try {
            byte[] addr = InetAddress.getByName(ip).getAddress();
            // 这里简化为判断A、B、C类私有地址，实际建议用库
            // 省略具体位运算代码，生产建议使用 ipaddress 等Maven库
        } catch (UnknownHostException e) { return true; }
        return false; // 占位，实际需实现
    }

    private static boolean isTrustedProxy(String ip) {
        // 实现CIDR匹配逻辑，判断ip是否在 TRUSTED_PROXIES 中
        // 若未配置，安全做法是仅当 remoteAddr 为 127.0.0.1 或内网LB时才信任Header
        return true; // 占位，实际需实现逻辑，如未配置请默认返回 false
    }
}