// MidjourneyResponseCodeEnum.java
package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyResponseCodeEnum {
    SUCCESS(1, "提交成功"),
    IN_QUEUE(22, "任务排队中"),
    PARAM_ERROR(21, "参数错误"),
    SYSTEM_ERROR(23, "系统错误"),
    ACCOUNT_UNAVAILABLE(24, "账号不可用"),
    INSUFFICIENT_BALANCE(25, "余额不足");

    private final Integer code;
    private final String message;

    MidjourneyResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MidjourneyResponseCodeEnum getByCode(Integer code) {
        for (MidjourneyResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}