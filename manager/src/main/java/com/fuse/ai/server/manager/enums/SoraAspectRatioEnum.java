package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SoraAspectRatioEnum {
    PORTRAIT("portrait", "竖屏"),
    LANDSCAPE("landscape", "横屏");

    private final String code;
    private final String description;

    SoraAspectRatioEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * JSON序列化时，使用code字段的值
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * JSON反序列化时，从code字符串转换为枚举
     */
    @JsonCreator
    public static SoraAspectRatioEnum fromCode(String code) {
        for (SoraAspectRatioEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code;
    }
}