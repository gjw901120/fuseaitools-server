package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum RunwayAspectRatioEnum {
    RATIO_16_9("16:9", "横屏16:9"),
    RATIO_4_3("4:3", "传统4:3"),
    RATIO_1_1("1:1", "正方形1:1"),
    RATIO_3_4("3:4", "竖屏3:4"),
    RATIO_9_16("9:16", "竖屏9:16");

    private final String ratio;
    private final String description;

    RunwayAspectRatioEnum(String ratio, String description) {
        this.ratio = ratio;
        this.description = description;
    }

    public static RunwayAspectRatioEnum getByRatio(String ratio) {
        for (RunwayAspectRatioEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }
}