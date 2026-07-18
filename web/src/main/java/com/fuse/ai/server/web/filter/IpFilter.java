package com.fuse.ai.server.web.filter;

import com.fuse.ai.server.web.common.utils.IpUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class IpFilter implements Filter {

    private static final ThreadLocal<String> IP_HOLDER = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String ip = IpUtils.getClientIp(httpReq); // 调用上面的工具类
        IP_HOLDER.set(ip);
        try {
            chain.doFilter(request, response);
        } finally {
            IP_HOLDER.remove(); // 清除，防止内存泄漏
        }
    }

    public static String getCurrentIp() {
        return IP_HOLDER.get();
    }
}

