package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fuse.ai.server.manager.enums.SubscriptionPackageEnum;
import com.fuse.ai.server.manager.enums.SubscriptionTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("subscription")
@Accessors(chain = true)
public class Subscription {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private Integer userId;

    private String stripeSubscriptionId;

    private SubscriptionTypeEnum type;

    private Integer status;

    @TableField("`package`")
    private SubscriptionPackageEnum subscriptionPackage;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建订阅对象的便捷方法
     */
    public static Subscription create(Integer orderId, Integer userId, String stripeSubscriptionId, SubscriptionTypeEnum type,
                                      Integer status, SubscriptionPackageEnum subscriptionPackage, LocalDate startDate, LocalDate endDate) {
        return Subscription.builder()
                .orderId(orderId)
                .userId(userId)
                .stripeSubscriptionId(stripeSubscriptionId)
                .type(type)
                .status(status)
                .subscriptionPackage(subscriptionPackage)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}