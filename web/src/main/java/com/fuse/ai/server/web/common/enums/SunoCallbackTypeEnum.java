package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

@Getter
public enum SunoCallbackTypeEnum {
    TEXT("text", "文本生成完成"),
    FIRST("first", "第一首完成"),
    COMPLETE("complete", "全部完成"),
    ERROR("error", "生成失败");

    private final String code;
    private final String desc;

    SunoCallbackTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SunoCallbackTypeEnum getByCode(String code) {
        for (SunoCallbackTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
