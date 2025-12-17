package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum ImageResponseCodeEnum {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "格式错误"),
    UNAUTHORIZED(401, "未授权"),
    INSUFFICIENT_CREDITS(402, "积分不足"),
    NOT_FOUND(404, "未找到"),
    VALIDATION_ERROR(422, "参数错误"),
    RATE_LIMIT(429, "超出限制"),
    SERVICE_UNAVAILABLE(455, "服务不可用"),
    SERVER_ERROR(500, "服务器错误"),
    CONNECTION_REFUSED(550, "连接被拒绝");

    private final Integer code;
    private final String message;

    ImageResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ImageResponseCodeEnum getByCode(Integer code) {
        for (ImageResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}