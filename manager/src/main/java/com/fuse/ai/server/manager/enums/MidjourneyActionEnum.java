package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyActionEnum {
    IMAGINE("IMAGINE", "创建图片"),
    UPSCALE("UPSCALE", "放大图片"),
    VARIATION("VARIATION", "变体生成"),
    ZOOM("ZOOM", "缩放图片"),
    PAN("PAN", "平移图片"),
    DESCRIBE("DESCRIBE", "图片描述"),
    BLEND("BLEND", "图片混合"),
    SHORTEN("SHORTEN", "缩短提示词"),
    SWAP_FACE("SWAP_FACE", "人脸替换");

    private final String code;
    private final String description;

    MidjourneyActionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MidjourneyActionEnum getByCode(String code) {
        for (MidjourneyActionEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}