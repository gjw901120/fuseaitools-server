// SubscriptionTypeEnum.java
package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum SubscriptionTypeEnum {
    WEEKLY(1, "周订阅"),
    MONTHLY(2, "月订阅"),
    YEARLY(3, "年订阅");

    private final Integer code;
    private final String description;

    SubscriptionTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}