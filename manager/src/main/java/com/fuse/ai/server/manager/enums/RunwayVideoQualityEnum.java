package com.fuse.ai.server.manager.enums;

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

    public static RunwayVideoQualityEnum getByQuality(String quality) {
        for (RunwayVideoQualityEnum value : values()) {
            if (value.getQuality().equals(quality)) {
                return value;
            }
        }
        return null;
    }
}