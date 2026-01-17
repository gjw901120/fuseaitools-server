package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("user_model_conversation")
@Accessors(chain = true)
public class UserModelConversation {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Builder.Default
    private String uuid = generateUuid();

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

    private static String generateUuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建对话对象的便捷方法
     */
    public static UserModelConversation create(Integer userId, String recordId, Integer modelId, String title) {
        return UserModelConversation.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .title(title)
                .build();
    }
}