package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Flux Kontext模型枚举
 */
@Getter
public enum FluxKontextModelEnum {

    FLUX_KONTEXT_PRO("flux-kontext-pro", "性能平衡的标准模型"),
    FLUX_KONTEXT_MAX("flux-kontext-max", "具有高级功能的增强模型");

    private final String code;
    private final String description;

    FluxKontextModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FluxKontextModelEnum getByCode(String code) {
        for (FluxKontextModelEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return FLUX_KONTEXT_PRO;
    }
}
