package com.fuse.ai.server.manager.enums;

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

    public static SoraFramesEnum getByCode(String code) {
        for (SoraFramesEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}