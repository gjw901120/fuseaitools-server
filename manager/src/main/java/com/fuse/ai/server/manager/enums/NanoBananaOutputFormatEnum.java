package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum NanoBananaOutputFormatEnum {
    JPEG("jpeg", "JPEG格式", "image/jpeg"),
    PNG("png", "PNG格式", "image/png");

    private final String format;
    private final String description;
    private final String mimeType;

    NanoBananaOutputFormatEnum(String format, String description, String mimeType) {
        this.format = format;
        this.description = description;
        this.mimeType = mimeType;
    }

    public static NanoBananaOutputFormatEnum getByFormat(String format) {
        for (NanoBananaOutputFormatEnum value : values()) {
            if (value.getFormat().equals(format)) {
                return value;
            }
        }
        return null;
    }
}