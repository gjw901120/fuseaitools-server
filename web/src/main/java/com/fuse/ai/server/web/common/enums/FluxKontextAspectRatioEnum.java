package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Flux Kontext图像宽高比枚举
 */
@Getter
public enum FluxKontextAspectRatioEnum {

    RATIO_21_9("21:9", "超宽屏", "电影显示、全景视图"),
    RATIO_16_9("16:9", "宽屏", "高清视频、桌面壁纸"),
    RATIO_4_3("4:3", "标准", "传统显示器、演示文稿"),
    RATIO_1_1("1:1", "正方形", "社交媒体帖子、头像"),
    RATIO_3_4("3:4", "竖版", "杂志版面、人像照片"),
    RATIO_9_16("9:16", "手机竖屏", "智能手机壁纸、故事");

    private final String ratio;
    private final String formatType;
    private final String commonUse;

    FluxKontextAspectRatioEnum(String ratio, String formatType, String commonUse) {
        this.ratio = ratio;
        this.formatType = formatType;
        this.commonUse = commonUse;
    }

    public static FluxKontextAspectRatioEnum getByRatio(String ratio) {
        for (FluxKontextAspectRatioEnum aspectRatio : values()) {
            if (aspectRatio.getRatio().equals(ratio)) {
                return aspectRatio;
            }
        }
        return RATIO_16_9;
    }
}