package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VeoModelEnum {
    VEO3("veo3", "标准模型"),
    VEO3_FAST("veo3_fast", "快速生成模型");

    private final String code;
    private final String description;

    VeoModelEnum(String code, String description) {
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
    public static VeoModelEnum fromCode(String code) {
        for (VeoModelEnum value : values()) {
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