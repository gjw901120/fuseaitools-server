package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

@Getter
public enum SunoRequestCodeEnum {
    SUCCESS(200, "成功"),
    VALIDATION_ERROR(400, "验证错误"),
    TIMEOUT(408, "超时"),
    CONFLICT(413, "冲突"),
    SERVER_ERROR(500, "服务器错误"),
    GENERATION_FAILED(501, "音频生成失败"),
    REFUNDED_ERROR(531, "生成失败，积分已退还");

    private final Integer code;
    private final String msg;

    SunoRequestCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static SunoRequestCodeEnum getByCode(Integer code) {
        for (SunoRequestCodeEnum responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return null;
    }
}