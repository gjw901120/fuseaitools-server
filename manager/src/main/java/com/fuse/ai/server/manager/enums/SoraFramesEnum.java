package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SoraFramesEnum {
    FRAMES_10("10", "10秒"),
    FRAMES_15("15", "15秒"),
    FRAMES_25("25", "25秒");

    private final String code;
    private final String description;

    SoraFramesEnum(String code, String description) {
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
    public static SoraFramesEnum fromCode(String code) {
        for (SoraFramesEnum value : values()) {
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