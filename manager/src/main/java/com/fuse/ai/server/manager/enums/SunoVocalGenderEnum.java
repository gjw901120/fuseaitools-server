package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum SunoVocalGenderEnum {
    MALE("m", "男声"),
    FEMALE("f", "女声");

    private final String code;
    private final String description;

    SunoVocalGenderEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SunoVocalGenderEnum getByCode(String code) {
        for (SunoVocalGenderEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}