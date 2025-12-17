package com.fuse.ai.server.manager.enums;

/**
 * 响应状态码枚举
 */
public enum ResponseCodeEnum {

    SUCCESS(200, "成功"),
    PROCESSING(400, "1080P正在处理中"),
    UNAUTHORIZED(401, "未授权"),
    INSUFFICIENT_CREDITS(402, "积分不足"),
    NOT_FOUND(404, "未找到"),
    VALIDATION_ERROR(422, "验证错误"),
    RATE_LIMIT(429, "请求限制"),
    IMAGE_ACCESS_DENIED(451, "获取图像失败"),
    SERVICE_UNAVAILABLE(455, "服务不可用"),
    SERVER_ERROR(500, "服务器错误"),
    GENERATION_FAILED(501, "生成失败"),
    FEATURE_DISABLED(505, "功能禁用");

    private final Integer code;
    private final String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCodeEnum getByCode(Integer code) {
        for (ResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}