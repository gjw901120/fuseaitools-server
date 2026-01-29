package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
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

    @JsonCreator
    public static ImageResponseCodeEnum deserialize(Integer code) {
        for (ImageResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        // 如果找不到对应的枚举值，可以选择返回一个默认值或抛出异常
        // 这里返回一个特殊的UNKNOWN枚举值，或者您可以选择其他处理方式
        throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Unknown code: " + code);
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