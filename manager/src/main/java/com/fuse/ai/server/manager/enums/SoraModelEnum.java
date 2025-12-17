package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum SoraModelEnum {
    SORA_2_TEXT_TO_VIDEO("sora-2-text-to-video", "Sora 2 文生视频"),
    SORA_2_IMAGE_TO_VIDEO("sora-2-image-to-video", "Sora 2 图生视频"),
    SORA_2_PRO_TEXT_TO_VIDEO("sora-2-pro-text-to-video", "Sora 2 Pro 文生视频"),
    SORA_2_PRO_IMAGE_TO_VIDEO("sora-2-pro-image-to-video", "Sora 2 Pro 图生视频"),
    SORA_WATERMARK_REMOVER("sora-watermark-remover", "Sora 水印移除"),
    SORA_2_PRO_STORYBOARD("sora-2-pro-storyboard", "Sora 2 Pro 故事板");

    private final String code;
    private final String description;

    SoraModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SoraModelEnum getByCode(String code) {
        for (SoraModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}