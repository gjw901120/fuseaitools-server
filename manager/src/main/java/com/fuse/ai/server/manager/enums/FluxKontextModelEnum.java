package com.fuse.ai.server.manager.enums;

import lombok.Getter;

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
        for (FluxKontextModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}