package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderTypeEnum {
    TOP_UP(1, "充值"),
    SUBSCRIPTION(2, "订阅");

    @EnumValue
    private final Integer code;
    private final String description;

    OrderTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}