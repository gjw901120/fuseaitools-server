package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("user_model_conversion")
@Accessors(chain = true)
public class UserModelConversion {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(fill = FieldFill.INSERT)
    private String uuid;

    private Integer userId;

    private String recordId;

    private Integer modelId;

    private String title;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建对话对象的便捷方法
     */
    public static UserModelConversion create(Integer userId, String recordId, Integer modelId, String title) {
        return UserModelConversion.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .title(title)
                .build();
    }
}