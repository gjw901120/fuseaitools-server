package com.fuse.ai.server.manager.enums;

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

    public static VeoAspectRatioEnum getByRatio(String ratio) {
        for (VeoAspectRatioEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }
}