package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SubscriptionTypeEnum {

    FREE(0, "Free"),
    WEEKLY(1, "Weekly"),
    MONTHLY(2, "Monthly"),
    YEARLY(3, "Yearly");

    @EnumValue
    private final Integer code;
    private final String description;

    SubscriptionTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SubscriptionTypeEnum of(Integer code) {
        for (SubscriptionTypeEnum value : SubscriptionTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return FREE;
    }
}