package com.fuse.ai.server.web.config.jwt;

import com.fuse.ai.server.web.common.utils.JwtTokenUtil;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        UserJwtDTO userJwtDTO = null;
        String jwtToken = null;

        // 从 Header 中提取 Token: "Bearer eyJhbGciOiJIUzI1NiIs..."
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // 1. 验证 Token 有效性
                if (jwtTokenUtil.validateToken(jwtToken)) {
                    // 2. 从 Token 中提取完整的 UserDetailVO
                    userJwtDTO = jwtTokenUtil.extractUserDetailVO(jwtToken);
                }
            } catch (Exception e) {
                logger.error("JWT Token 处理失败", e);
            }
        }

        // 3. 将 UserDetailVO 设置到 Spring Security 上下文中
        if (userJwtDTO != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 创建 Authentication 对象（权限列表为空）
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userJwtDTO, // principal 现在是 UserDetailVO
                            null, // credentials 为 null
                            Collections.emptyList() // 无权限
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}