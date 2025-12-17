// SubscriptionConfig.java
package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("subscription_config")
public class SubscriptionConfig {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer type;

    @TableField("`package`")
    private Integer subscriptionPackage;

    private BigDecimal discount;

    private BigDecimal cost;

    private BigDecimal credits;

    private BigDecimal giftRatio;

    private BigDecimal giftCredits;

    private BigDecimal totalCredits;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}