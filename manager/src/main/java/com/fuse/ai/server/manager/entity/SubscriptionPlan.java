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
    private BigDecimal credits = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal giftCredits = BigDecimal.ZERO;

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
    public static SubscriptionPlan create(Integer userId, Integer subscriptionId, LocalDate startDate, LocalDate endDate, BigDecimal credits,
                                          BigDecimal giftCredits, Integer status) {
        return SubscriptionPlan.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .startDate(startDate)
                .endDate(endDate)
                .credits(credits != null ? credits : BigDecimal.ZERO)
                .giftCredits(giftCredits != null ? giftCredits : BigDecimal.ZERO)
                .status(status)
                .build();
    }
}