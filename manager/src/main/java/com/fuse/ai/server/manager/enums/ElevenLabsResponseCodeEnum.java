package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum ElevenLabsResponseCodeEnum {
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未授权"),
    INSUFFICIENT_CREDITS(402, "积分不足"),
    NOT_FOUND(404, "未找到"),
    VALIDATION_ERROR(422, "验证错误"),
    RATE_LIMIT(429, "频率限制"),
    SERVICE_UNAVAILABLE(455, "服务不可用"),
    SERVER_ERROR(500, "服务器错误");

    private final Integer code;
    private final String message;

    ElevenLabsResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ElevenLabsResponseCodeEnum getByCode(Integer code) {
        for (ElevenLabsResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}