package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fuse.ai.server.manager.enums.OrderStatusEnum;
import com.fuse.ai.server.manager.enums.OrderTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("`order`")
@Accessors(chain = true)
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Builder.Default
    private String uuid = generateUuid();

    private Integer userId;

    private OrderTypeEnum type;

    private Integer couponId;

    @Builder.Default
    private BigDecimal paymentAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal giftAmount = BigDecimal.ZERO;

    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.UNPAID;

    private String stripePaymentIntentId;

    private String stripeCustomerId;

    private String stripeInvoiceId;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 生成UUID
     */
    private static String generateUuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建订单对象的便捷方法
     */
    public static Order create(Integer userId, OrderTypeEnum type, Integer couponId, BigDecimal paymentAmount, BigDecimal discountAmount,
                               BigDecimal amount, BigDecimal giftAmount, OrderStatusEnum status,
                               String stripePaymentIntentId, String stripeCustomerId, String stripeInvoiceId) {
        return Order.builder()
                .userId(userId)
                .type(type)
                .couponId(couponId)
                .paymentAmount(paymentAmount != null ? paymentAmount : BigDecimal.ZERO)
                .discountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO)
                .amount(amount != null ? amount : BigDecimal.ZERO)
                .giftAmount(giftAmount != null ? giftAmount : BigDecimal.ZERO)
                .status(status != null ? status : OrderStatusEnum.UNPAID)
                .stripePaymentIntentId(stripePaymentIntentId)
                .stripeCustomerId(stripeCustomerId)
                .stripeInvoiceId(stripeInvoiceId)
                .build();
    }
}