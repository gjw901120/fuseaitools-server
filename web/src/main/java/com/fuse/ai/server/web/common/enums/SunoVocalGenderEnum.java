package com.fuse.ai.server.web.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    /**
     * 序列化时使用 code 字段（小写）
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * 反序列化时使用 code 字段查找枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SunoVocalGenderEnum getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (SunoVocalGenderEnum gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return null;
    }
}