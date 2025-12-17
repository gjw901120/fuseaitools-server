package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Flux Kontext输出格式枚举
 */
@Getter
public enum FluxKontextOutputFormatEnum {

    JPEG("jpeg", "JPEG格式"),
    PNG("png", "PNG格式");

    private final String format;
    private final String description;

    FluxKontextOutputFormatEnum(String format, String description) {
        this.format = format;
        this.description = description;
    }

    public static FluxKontextOutputFormatEnum getByFormat(String format) {
        for (FluxKontextOutputFormatEnum outputFormat : values()) {
            if (outputFormat.getFormat().equals(format)) {
                return outputFormat;
            }
        }
        return JPEG;
    }
}