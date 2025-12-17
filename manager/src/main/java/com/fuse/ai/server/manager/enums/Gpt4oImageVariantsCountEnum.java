package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum Gpt4oImageVariantsCountEnum {
    ONE(1, "1个变体"),
    TWO(2, "2个变体"),
    FOUR(4, "4个变体");

    private final Integer count;
    private final String description;

    Gpt4oImageVariantsCountEnum(Integer count, String description) {
        this.count = count;
        this.description = description;
    }

    public static Gpt4oImageVariantsCountEnum getByCount(Integer count) {
        for (Gpt4oImageVariantsCountEnum value : values()) {
            if (value.getCount().equals(count)) {
                return value;
            }
        }
        return null;
    }
}