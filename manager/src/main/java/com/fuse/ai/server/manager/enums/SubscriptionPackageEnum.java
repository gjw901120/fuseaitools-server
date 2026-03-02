package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SubscriptionPackageEnum {
    NONE(0, ""),
    BASIC(1, "Basic"),
    PRO(2, "Pro"),
    ULTIMATE(3, "Ultimate");

    @EnumValue
    private final Integer code;
    private final String description;

    SubscriptionPackageEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SubscriptionPackageEnum of(Integer code) {
        for (SubscriptionPackageEnum value : SubscriptionPackageEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return NONE;
    }
}