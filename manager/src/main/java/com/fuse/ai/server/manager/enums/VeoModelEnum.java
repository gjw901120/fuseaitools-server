package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum VeoModelEnum {
    VEO3("veo3", "标准模型"),
    VEO3_FAST("veo3_fast", "快速生成模型");

    private final String code;
    private final String description;

    VeoModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static VeoModelEnum getByCode(String code) {
        for (VeoModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}