package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("order_refund")
public class OrderRefund {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Builder.Default
    private String uuid = generateUuid();

    private Integer orderId;

    private String stripeRefundId;

    private Integer status;

    private Integer type;

    private BigDecimal price;

    private String reason;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    private static String generateUuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public static OrderRefund create(Integer orderId, String stripeRefundId, Integer status, Integer type, BigDecimal price, String reason) {
        return OrderRefund.builder()
                .orderId(orderId)
                .stripeRefundId(stripeRefundId)
                .status(status)
                .type(type)
                .price(price)
                .reason(reason)
                .build();
    }
}