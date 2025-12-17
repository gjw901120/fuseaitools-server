package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyDimensionsEnum {
    PORTRAIT("PORTRAIT", "2:3 比例", "竖版"),
    SQUARE("SQUARE", "1:1 比例", "正方形"),
    LANDSCAPE("LANDSCAPE", "3:2 比例", "横版");

    private final String code;
    private final String description;
    private final String type;

    MidjourneyDimensionsEnum(String code, String description, String type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public static MidjourneyDimensionsEnum getByCode(String code) {
        for (MidjourneyDimensionsEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}