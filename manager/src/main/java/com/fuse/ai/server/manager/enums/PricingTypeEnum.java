package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum PricingTypeEnum {
    ONCE(1, "一次性"),
    TOKEN(2, "按token"),
    CHARACTER(3, "按字符"),
    DURATION(4, "按时长");

    private final Integer code;
    private final String description;

    PricingTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}