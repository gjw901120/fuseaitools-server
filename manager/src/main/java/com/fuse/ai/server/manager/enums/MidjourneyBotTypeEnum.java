package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyBotTypeEnum {
    MID_JOURNEY("MID_JOURNEY", "Midjourney 模型"),
    NIJI_JOURNEY("NIJI_JOURNEY", "Niji Journey 模型");

    private final String code;
    private final String description;

    MidjourneyBotTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MidjourneyBotTypeEnum getByCode(String code) {
        for (MidjourneyBotTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}