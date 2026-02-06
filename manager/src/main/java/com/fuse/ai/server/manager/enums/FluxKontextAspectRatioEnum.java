package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FluxKontextAspectRatioEnum {
    RATIO_21_9("21:9", "超宽屏", "电影显示、全景视图"),
    RATIO_16_9("16:9", "宽屏", "高清视频、桌面壁纸"),
    RATIO_4_3("4:3", "标准", "传统显示器、演示文稿"),
    RATIO_1_1("1:1", "正方形", "社交媒体帖子、头像"),
    RATIO_3_4("3:4", "竖版", "杂志版面、人像照片"),
    RATIO_9_16("9:16", "手机竖屏", "智能手机壁纸、故事");

    private final String ratio;
    private final String type;
    private final String usage;

    FluxKontextAspectRatioEnum(String ratio, String type, String usage) {
        this.ratio = ratio;
        this.type = type;
        this.usage = usage;
    }

    @JsonValue  // 序列化时使用
    public String getRatio() {
        return ratio;
    }

    @JsonCreator  // 反序列化时使用
    public static FluxKontextAspectRatioEnum fromRatio(String ratio) {
        for (FluxKontextAspectRatioEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 重写 toString() 方法，返回 ratio
     * 这样在打印日志或调用 toString() 时显示 ratio
     */
    @Override
    public String toString() {
        return ratio;
    }
}