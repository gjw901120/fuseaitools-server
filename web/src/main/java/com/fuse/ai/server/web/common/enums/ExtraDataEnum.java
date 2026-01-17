package com.fuse.ai.server.web.common.enums;

/**
 * Veo视频生成模型枚举
 */
public enum ExtraDataEnum {

    DURATION_QUALITY("duration_quality", "时间 & 质量"),
    DURATION_SIZE("duration_size", "时间 & 大小"),
    DURATION("duration", "时间"),
    QUALITY("quality", "质量"),
    ELE_DURATION("ele_duration", "ele 时间"),
    ELE_CHARACTER("ele_character", "ele 字符");

    private final String code;
    private final String description;

    ExtraDataEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ExtraDataEnum getByCode(String code) {
        for (ExtraDataEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return DURATION_QUALITY;
    }
}