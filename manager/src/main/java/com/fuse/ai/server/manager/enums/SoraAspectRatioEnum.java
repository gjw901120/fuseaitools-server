package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum SoraAspectRatioEnum {
    PORTRAIT("portrait", "竖屏"),
    LANDSCAPE("landscape", "横屏");

    private final String code;
    private final String description;

    SoraAspectRatioEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SoraAspectRatioEnum getByCode(String code) {
        for (SoraAspectRatioEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}