package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum NanoBananaAspectRatioEnum {
    RATIO_1_1("1:1", "正方形", "社交媒体、头像"),
    RATIO_9_16("9:16", "手机竖屏", "手机壁纸、短视频"),
    RATIO_16_9("16:9", "宽屏", "桌面壁纸、视频"),
    RATIO_3_4("3:4", "竖版", "杂志、肖像"),
    RATIO_4_3("4:3", "标准", "传统照片"),
    RATIO_3_2("3:2", "经典照片", "摄影"),
    RATIO_2_3("2:3", "竖版照片", "人像摄影"),
    RATIO_5_4("5:4", "宽幅", "特殊比例"),
    RATIO_4_5("4:5", "竖幅", "特殊比例"),
    RATIO_21_9("21:9", "超宽屏", "电影、全景"),
    RATIO_AUTO("auto", "自动", "系统自动选择");

    private final String ratio;
    private final String type;
    private final String usage;

    NanoBananaAspectRatioEnum(String ratio, String type, String usage) {
        this.ratio = ratio;
        this.type = type;
        this.usage = usage;
    }

    public static NanoBananaAspectRatioEnum getByRatio(String ratio) {
        for (NanoBananaAspectRatioEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }
}