package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyModeEnum {
    RELAX("RELAX", "放松模式"),
    FAST("FAST", "快速模式"),
    TURBO("TURBO", "涡轮模式");

    private final String code;
    private final String description;

    MidjourneyModeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MidjourneyModeEnum getByCode(String code) {
        for (MidjourneyModeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}