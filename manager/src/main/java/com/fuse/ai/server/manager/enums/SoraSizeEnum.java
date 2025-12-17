package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum SoraSizeEnum {
    STANDARD("standard", "标准"),
    HIGH("high", "高清");

    private final String code;
    private final String description;

    SoraSizeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SoraSizeEnum getByCode(String code) {
        for (SoraSizeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}