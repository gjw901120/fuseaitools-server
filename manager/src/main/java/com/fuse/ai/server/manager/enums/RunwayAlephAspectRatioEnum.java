package com.fuse.ai.server.manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RunwayAlephAspectRatioEnum {
    RATIO_16_9("16:9", "横屏16:9"),
    RATIO_9_16("9:16", "竖屏9:16"),
    RATIO_4_3("4:3", "传统4:3"),
    RATIO_3_4("3:4", "竖屏3:4"),
    RATIO_1_1("1:1", "正方形1:1"),
    RATIO_21_9("21:9", "超宽屏21:9");

    private final String ratio;
    private final String description;

    RunwayAlephAspectRatioEnum(String ratio, String description) {
        this.ratio = ratio;
        this.description = description;
    }

    /**
     * JSON序列化时，使用ratio字段的值
     */
    @JsonValue
    public String getRatio() {
        return ratio;
    }

    /**
     * JSON反序列化时，从ratio字符串转换为枚举
     */
    @JsonCreator
    public static RunwayAlephAspectRatioEnum fromRatio(String ratio) {
        for (RunwayAlephAspectRatioEnum value : values()) {
            if (value.getRatio().equals(ratio)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 重写 toString() 方法，返回ratio
     * 这样在打印日志或调用 toString() 时显示小写
     */
    @Override
    public String toString() {
        return ratio;
    }
}