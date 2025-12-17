package com.fuse.ai.server.web.common.enums;

/**
 * Veo视频宽高比枚举
 */
public enum VeoAspectRatioEnum {

    RATIO_16_9("16:9", "横屏视频格式"),
    RATIO_9_16("9:16", "竖屏视频格式"),
    AUTO("Auto", "自动匹配");

    private final String code;
    private final String description;

    VeoAspectRatioEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VeoAspectRatioEnum getByCode(String code) {
        for (VeoAspectRatioEnum ratio : values()) {
            if (ratio.getCode().equals(code)) {
                return ratio;
            }
        }
        return RATIO_16_9;
    }
}