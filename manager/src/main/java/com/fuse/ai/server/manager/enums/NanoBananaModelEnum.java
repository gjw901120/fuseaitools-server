package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum NanoBananaModelEnum {
    NANO_BANANA_GENERATE("google/nano-banana", "Nano Banana生成模型"),
    NANO_BANANA_EDIT("google/nano-banana-edit", "Nano Banana编辑模型");

    private final String code;
    private final String description;

    NanoBananaModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static NanoBananaModelEnum getByCode(String code) {
        for (NanoBananaModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}