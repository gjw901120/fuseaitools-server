package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fuse.ai.server.manager.enums.ModelTypeEnum;
import com.fuse.ai.server.manager.enums.PricingTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("models")
public class Models {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private String name;

    private String requestName;

    private String requestToken;

    private String originalToken;

    private ModelTypeEnum type;

    private Integer isPricingRules;

    private PricingTypeEnum pricingType;

    private Integer isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}