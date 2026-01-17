package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SubscriptionPackageEnum {
    NONE(0, "无"),
    BASIC(1, "基础版"),
    PRO(2, "专业版"),
    ULTIMATE(3, "无限版");

    @EnumValue
    private final Integer code;
    private final String description;

    SubscriptionPackageEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}