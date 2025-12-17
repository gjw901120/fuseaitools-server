package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@TableName("user_credits")
@Accessors(chain = true)
public class UserCredits {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @Builder.Default
    private BigDecimal credits = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal blockCredits = BigDecimal.ZERO;

    private Integer type;

    private Integer status;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建用户积分对象的便捷方法
     */
    public static UserCredits create(Integer userId, BigDecimal credits, BigDecimal blockCredits, Integer type, Integer status) {
        return UserCredits.builder()
                .userId(userId)
                .credits(credits != null ? credits : BigDecimal.ZERO)
                .blockCredits(blockCredits != null ? blockCredits : BigDecimal.ZERO)
                .type(type)
                .status(status)
                .build();
    }
}