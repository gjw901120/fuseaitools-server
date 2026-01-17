package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("models_pricing_rules")
public class ModelsPricingRules {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer modelId;

    private Integer pricingId;

    private Integer duration;

    private String quality;

    private String size;

    private Integer batchSize;

    private Integer speed;

    private String scene;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}