package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @Override
    public String toString() {
        return format;
    }

    /**
     * JSON序列化时，使用format字段的值
     */
    @JsonValue
    public String getFormat() {
        return format;
    }

    /**
     * JSON反序列化时，从format字符串转换为枚举
     */
    @JsonCreator
    public static NanoBananaOutputFormatEnum fromFormat(String format) {
        for (NanoBananaOutputFormatEnum value : values()) {
            if (value.getFormat().equalsIgnoreCase(format)) {
                return value;
            }
        }
        return null;
    }
}