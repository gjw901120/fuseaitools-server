package com.fuse.ai.server.web.common.enums;

/**
 * Veo视频生成模型枚举
 */
public enum VeoModelEnum {

    VEO3("veo3", "VEO3.1标准模型"),
    VEO3_FAST("veo3_fast", "VEO3.1快速生成模型");

    private final String code;
    private final String description;

    VeoModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VeoModelEnum getByCode(String code) {
        for (VeoModelEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return VEO3_FAST;
    }
}