package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("recharge_config")
public class RechargeConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 费用
     */
    private BigDecimal cost;

    /**
     * 积分
     */
    private BigDecimal credits;

    /**
     * 赠送比例
     */
    private BigDecimal giftRatio;

    /**
     * 赠送积分
     */
    private BigDecimal giftCredits;

    /**
     * 总积分
     */
    private BigDecimal totalCredits;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}