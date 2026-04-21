package com.fuse.ai.server.web.common.enums;

/**
 * Veo视频生成模型枚举
 */
public enum ExtraDataEnum {

    DURATION_QUALITY("duration_quality", "时间 & 质量"),

    PER_DURATION_QUALITY("per_duration_quality", "每秒时间 & 质量"),
    DURATION_SIZE("duration_size", "时间 & 大小"),

    PER_DURATION_SIZE("per_duration_size", "每秒时间 & 大小"),
    DURATION_SCENE_SIZE("duration_scene_size", "时间 & 场景 & 大小"),

    PER_DURATION_SCENE_SIZE("per_duration_scene_size", "每秒时间 & 场景 & 大小"),

    DURATION_QUALITY_SCENE("duration_quality_scene", "时间 & 质量 & 场景"),

    PER_DURATION_QUALITY_SCENE("per_duration_quality_scene", "每秒时间 & 质量 & 场景"),
    DURATION_SCENE("duration_scene", "时间 & 场景"),
    DURATION("duration", "时间"),
    QUALITY("quality", "质量"),

    SIZE("size", "大小"),

    PER_BATCH_SIZE("per_batch_size", "批量大小"),

    SPEED("speed", "速度"),
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