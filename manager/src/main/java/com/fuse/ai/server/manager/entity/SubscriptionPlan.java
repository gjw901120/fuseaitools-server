package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("subscription_plan")
@Accessors(chain = true)
public class SubscriptionPlan {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer subscriptionId;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal giftAmount = BigDecimal.ZERO;

    private Integer status;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建订阅计划对象的便捷方法
     */
    public static SubscriptionPlan create(Integer userId, Integer subscriptionId, LocalDate startDate, LocalDate endDate, BigDecimal amount,
                                          BigDecimal giftAmount, Integer status) {
        return SubscriptionPlan.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .startDate(startDate)
                .endDate(endDate)
                .amount(amount != null ? amount : BigDecimal.ZERO)
                .giftAmount(giftAmount != null ? giftAmount : BigDecimal.ZERO)
                .status(status)
                .build();
    }
}