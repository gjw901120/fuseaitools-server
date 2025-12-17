package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum VeoGenerationTypeEnum {
    TEXT_2_VIDEO("TEXT_2_VIDEO", "文生视频"),
    FIRST_AND_LAST_FRAMES_2_VIDEO("FIRST_AND_LAST_FRAMES_2_VIDEO", "首尾帧生视频"),
    REFERENCE_2_VIDEO("REFERENCE_2_VIDEO", "参考图生视频");

    private final String code;
    private final String description;

    VeoGenerationTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static VeoGenerationTypeEnum getByCode(String code) {
        for (VeoGenerationTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}