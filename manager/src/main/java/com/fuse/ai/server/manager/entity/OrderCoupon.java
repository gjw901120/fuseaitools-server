package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_coupon")
public class OrderCoupon {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String stripePriceId;

    private String stripeCouponId;

    private Integer subscriptionType;

    private Integer subscriptionPackage;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}