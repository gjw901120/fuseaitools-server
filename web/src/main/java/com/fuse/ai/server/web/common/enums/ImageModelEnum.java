package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * 图像生成模型枚举
 */
@Getter
public enum ImageModelEnum {

    GPT4O_IMAGE("gpt4o-image", "GPT-4O图像生成模型"),
    FLUX_KONTEXT("flux-kontext", "Flux Kontext图像生成模型"),
    NANO_BANANA("google/nano-banana", "Nano Banana图像生成模型");

    private final String code;
    private final String description;

    ImageModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public static ImageModelEnum getByCode(String code) {
        for (ImageModelEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return null;
    }
}
