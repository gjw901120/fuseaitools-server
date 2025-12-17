package com.fuse.ai.server.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PosFlowStatusEnum {

    UNPAY(0, "未支付"),
    PAID(10, "已支付"),
    REFUND(20, "已退款");

    private Integer code;

    private String msg;
}