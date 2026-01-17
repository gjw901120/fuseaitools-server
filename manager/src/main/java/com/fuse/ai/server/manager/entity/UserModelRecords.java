package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@TableName(value = "user_model_records", autoResultMap = true)
@Accessors(chain = true)
public class UserModelRecords {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Builder.Default
    private String uuid = generateUuid();

    @TableField("user_id")
    private Integer userId;

    @TableField("model_id")
    private Integer modelId;

    @TableField("is_completed")
    private Integer isCompleted;

    private String title;

    @TableField("gmt_completed")
    private LocalDateTime gmtCompleted;

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
     * 创建新记录的便捷方法
     */
    public static UserModelRecords create(Integer userId, Integer modelId, String title, Integer isCompleted) {
        return UserModelRecords.builder()
                .userId(userId)
                .modelId(modelId)
                .title(title)
                .isCompleted(isCompleted)
                .build();
    }


}