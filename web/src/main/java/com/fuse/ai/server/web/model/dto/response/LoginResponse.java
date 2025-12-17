package com.fuse.ai.server.web.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    /**
     * 便捷创建方法
     * @param token 认证令牌
     * @return LoginResponse实例
     */
    public static LoginResponse create(String token) {
        return LoginResponse.builder()
                .token(token)
                .build();
    }

}
