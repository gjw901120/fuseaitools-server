package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RunwayVideoQualityEnum {
    P_720("720p", "720P分辨率"),
    P_1080("1080p", "1080P分辨率");

    private final String quality;
    private final String description;

    RunwayVideoQualityEnum(String quality, String description) {
        this.quality = quality;
        this.description = description;
    }

    /**
     * JSON序列化时，使用quality字段的值
     */
    @JsonValue
    public String getQuality() {
        return quality;
    }

    /**
     * JSON反序列化时，从quality字符串转换为枚举
     */
    @JsonCreator
    public static RunwayVideoQualityEnum fromQuality(String quality) {
        for (RunwayVideoQualityEnum value : values()) {
            if (value.getQuality().equals(quality)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return quality;
    }
}