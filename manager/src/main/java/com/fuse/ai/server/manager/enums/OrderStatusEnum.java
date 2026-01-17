package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    UNPAID(1, "未付款"),
    PAID(2, "已支付"),
    FAILED(3, "付款失败"),
    REFUNDED(4, "已退款");

    @EnumValue
    private final Integer code;
    private final String description;

    OrderStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}