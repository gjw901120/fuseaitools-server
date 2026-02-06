package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    /**
     * JSON序列化时，使用code字段的值
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * JSON反序列化时，从code字符串转换为枚举
     */
    @JsonCreator
    public static SoraModelEnum fromCode(String code) {
        for (SoraModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code;
    }
}