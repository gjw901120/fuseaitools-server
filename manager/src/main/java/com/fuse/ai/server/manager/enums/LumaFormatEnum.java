package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum LumaFormatEnum {
    MP4("mp4", "MP4格式"),
    MOV("mov", "MOV格式"),
    AVI("avi", "AVI格式");

    private final String format;
    private final String description;

    LumaFormatEnum(String format, String description) {
        this.format = format;
        this.description = description;
    }

    public static LumaFormatEnum getByFormat(String format) {
        for (LumaFormatEnum value : values()) {
            if (value.getFormat().equalsIgnoreCase(format)) {
                return value;
            }
        }
        return null;
    }

    public static boolean isSupported(String format) {
        return getByFormat(format) != null;
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