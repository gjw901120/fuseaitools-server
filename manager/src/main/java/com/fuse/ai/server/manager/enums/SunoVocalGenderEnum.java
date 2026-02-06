package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SunoVocalGenderEnum {
    M("m", "男声"),
    F("f", "女声");

    private final String code;
    private final String description;

    SunoVocalGenderEnum(String code, String description) {
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
    /**
     * JSON反序列化时，从code字符串转换为枚举
     */
    @JsonCreator
    public static SunoVocalGenderEnum fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (SunoVocalGenderEnum value : values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 重写 toString() 方法，返回小写的 code
     * 这样在打印日志或调用 toString() 时显示小写
     */
    @Override
    public String toString() {
        return code;
    }
}