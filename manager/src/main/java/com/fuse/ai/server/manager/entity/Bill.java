package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fuse.ai.server.manager.enums.BillStatusEnum;
import com.fuse.ai.server.manager.enums.UserCreditTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 消费记录实体类
 * 对应数据库表：bill
 */
@Data
@Builder
@TableName("bill")
@Accessors(chain = true)
public class Bill {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 模型ID
     */
    private Integer modelId;

    /**
     * 记录ID（UUID格式）
     */
    private String recordId;

    /**
     * 定价ID
     */
    private Integer pricingId;

    /**
     * 消息ID
     */
    private Integer messageId;

    /**
     * 积分类型：1.充值，2.订阅，3.充值+订阅
     */
    private UserCreditTypeEnum userCreditType;

    /**
     * 扣除订阅积分
     */
    @Builder.Default
    private BigDecimal subscriptionDeductCredits = BigDecimal.ZERO;

    /**
     * 扣除充值积分
     */
    @Builder.Default
    private BigDecimal rechargeDeductCredits = BigDecimal.ZERO;

    /**
     * 应扣除积分（折扣前总额）
     */
    @Builder.Default
    private BigDecimal shouldDeductCredits = BigDecimal.ZERO;

    /**
     * 订阅原始积分
     */
    @Builder.Default
    private BigDecimal subscriptionOriginCredits = BigDecimal.ZERO;

    /**
     * 充值原始积分
     */
    @Builder.Default
    private BigDecimal rechargeOriginCredits = BigDecimal.ZERO;

    /**
     * 折扣（0.00-1.00）
     */
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    /**
     * 是否免费：1.是，0.否
     */
    @Builder.Default
    private Integer isFree = 0;


    /**
     * 是否删除
     */
    @Builder.Default
    private Integer isDel = 0;

    private BillStatusEnum status;

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

    /**
     * 便捷创建方法 - 完整版本
     */
    public static Bill create(Integer userId, String recordId, Integer modelId,
                              Integer pricingId, Integer messageId,
                              UserCreditTypeEnum userCreditType,
                              BigDecimal subscriptionDeductCredits,
                              BigDecimal rechargeDeductCredits,
                              BigDecimal shouldDeductCredits,
                              BigDecimal subscriptionOriginCredits,
                              BigDecimal rechargeOriginCredits,
                              BigDecimal discount,
                              BillStatusEnum status,
                              Integer isFree) {
        return Bill.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .pricingId(pricingId)
                .messageId(messageId)
                .userCreditType(userCreditType)
                .subscriptionDeductCredits(subscriptionDeductCredits)
                .rechargeDeductCredits(rechargeDeductCredits)
                .shouldDeductCredits(shouldDeductCredits)
                .subscriptionOriginCredits(subscriptionOriginCredits)
                .rechargeOriginCredits(rechargeOriginCredits)
                .discount(discount)
                .status(status)
                .isFree(isFree)
                .build();
    }

}