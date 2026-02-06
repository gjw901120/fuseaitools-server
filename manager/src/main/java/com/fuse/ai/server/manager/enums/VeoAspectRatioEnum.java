package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VeoAspectRatioEnum {
    RATIO_16_9("16:9", "横屏16:9"),
    RATIO_9_16("9:16", "竖屏9:16"),
    AUTO("Auto", "自动匹配");

    private final String ratio;
    private final String description;

    VeoAspectRatioEnum(String ratio, String description) {
        this.ratio = ratio;
        this.description = description;
    }

    /**
     * JSON序列化时，使用ratio字段的值
     */
    @JsonValue
    public String getRatio() {
        return ratio;
    }

    /**
     * JSON反序列化时，从ratio字符串转换为枚举
     */
    @JsonCreator
    public static VeoAspectRatioEnum fromRatio(String ratio) {
        for (VeoAspectRatioEnum value : values()) {
            if (value.getRatio().equalsIgnoreCase(ratio)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return ratio;
    }
}