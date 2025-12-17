package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum Gpt4oImageImageSizeEnum {
    SIZE_1_1("1:1", "正方形"),
    SIZE_3_2("3:2", "横屏3:2"),
    SIZE_2_3("2:3", "竖屏2:3");

    private final String ratio;
    private final String description;

    Gpt4oImageImageSizeEnum(String ratio, String description) {
        this.ratio = ratio;
        this.description = description;
    }

    public static Gpt4oImageImageSizeEnum getByRatio(String ratio) {
        for (Gpt4oImageImageSizeEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }
}