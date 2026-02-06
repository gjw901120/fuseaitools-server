package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
    public static FluxKontextOutputFormatEnum fromFormat(String format) {
        for (FluxKontextOutputFormatEnum value : values()) {
            if (value.getFormat().equalsIgnoreCase(format)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 重写 toString() 方法，返回小写的 code
     * 这样在打印日志或调用 toString() 时显示小写
     */
    @Override
    public String toString() {
        return format;
    }
}