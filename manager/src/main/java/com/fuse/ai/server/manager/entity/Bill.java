package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fuse.ai.server.manager.enums.UserCreditTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("bill")
@Accessors(chain = true)
public class Bill {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String recordId;

    private Integer modelId;

    private Integer pricingId;

    private Integer messageId;

    private UserCreditTypeEnum userCreditType;

    @Builder.Default
    private BigDecimal actualDeductCredits = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal deductCredits = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal originCredits = BigDecimal.ZERO;

    private BigDecimal discount;

    @Builder.Default
    private Integer isFree = 0;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建账单对象的便捷方法
     */
    public static Bill create(Integer userId, String recordId, Integer modelId, Integer pricingId, Integer messageId, UserCreditTypeEnum userCreditType,
                              BigDecimal actualDeductCredits, BigDecimal deductCredits, BigDecimal originCredits, BigDecimal discount, Integer isFree) {
        return Bill.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .pricingId(pricingId)
                .messageId(messageId)
                .userCreditType(userCreditType)
                .actualDeductCredits(actualDeductCredits != null ? actualDeductCredits : BigDecimal.ZERO)
                .deductCredits(deductCredits != null ? deductCredits : BigDecimal.ZERO)
                .originCredits(originCredits != null ? originCredits : BigDecimal.ZERO)
                .discount(discount)
                .isFree(isFree != null ? isFree : 0)
                .build();
    }
}