package com.fuse.ai.server.inner.service.impl;

import com.fuse.ai.server.inner.service.UserService;
import com.fuse.ai.server.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {
    @Value("jwt.secret")
    private String jwtSecretKey;

    @Override
    public BigInteger authorizationAndGetUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return JwtUtil.getUserId(token);
    }
}
