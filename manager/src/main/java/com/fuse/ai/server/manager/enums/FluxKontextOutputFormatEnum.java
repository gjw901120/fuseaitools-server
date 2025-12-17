package com.fuse.ai.server.manager.enums;

import lombok.Getter;

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
        for (FluxKontextOutputFormatEnum value : values()) {
            if (value.getFormat().equals(format)) {
                return value;
            }
        }
        return null;
    }
}