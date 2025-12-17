package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum FluxKontextSafetyToleranceEnum {
    LEVEL_0(0, "最严格"),
    LEVEL_1(1, "严格"),
    LEVEL_2(2, "平衡"),
    LEVEL_3(3, "适中"),
    LEVEL_4(4, "宽松"),
    LEVEL_5(5, "较宽松"),
    LEVEL_6(6, "更宽松");

    private final Integer level;
    private final String description;

    FluxKontextSafetyToleranceEnum(Integer level, String description) {
        this.level = level;
        this.description = description;
    }

    public static FluxKontextSafetyToleranceEnum getByLevel(Integer level) {
        for (FluxKontextSafetyToleranceEnum value : values()) {
            if (value.getLevel().equals(level)) {
                return value;
            }
        }
        return null;
    }
}