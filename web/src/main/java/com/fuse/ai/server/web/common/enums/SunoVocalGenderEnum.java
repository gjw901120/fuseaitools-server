package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Suno人声性别枚举
 */
@Getter
public enum SunoVocalGenderEnum {

    M("m", "男声"),
    F("f", "女声");

    private final String code;
    private final String description;

    SunoVocalGenderEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SunoVocalGenderEnum getByCode(String code) {
        for (SunoVocalGenderEnum gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return null;
    }
}