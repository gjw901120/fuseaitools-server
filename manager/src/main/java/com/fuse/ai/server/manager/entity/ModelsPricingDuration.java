package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("models_pricing_duration")
public class ModelsPricingDuration {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer modelId;

    private Integer duration;

    private BigDecimal lossCreadits;

    private BigDecimal creadits;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}