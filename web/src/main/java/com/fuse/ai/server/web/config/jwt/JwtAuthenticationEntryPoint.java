package com.fuse.ai.server.web.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, BaseException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 直接写入自定义的JSON响应
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("errorMessage", "User Authorization failed");
        body.put("errorCode", "A0220"); // 您的自定义错误码
        body.put("data", new Object());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);

    }
}