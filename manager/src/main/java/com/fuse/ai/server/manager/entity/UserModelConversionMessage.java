package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fuse.ai.server.manager.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@TableName("user_model_conversion_message")
@Accessors(chain = true)
public class UserModelConversionMessage {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer modelId;

    private String conversionId;

    private UserRoleEnum role;

    private String message;

    @TableField(value = "files", typeHandler = JacksonTypeHandler.class)
    private List<String> files;

    @TableField(value = "third_files", typeHandler = JacksonTypeHandler.class)
    private List<String> thirdFiles;

    private Integer promptTokens;

    private Integer completionTokens;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建消息对象的便捷方法
     */
    public static UserModelConversionMessage create(Integer userId, Integer modelId,
                                                    String conversionId, UserRoleEnum role,
                                                    String message, List<String> files,
                                                    Integer promptTokens, Integer completionTokens) {
        return UserModelConversionMessage.builder()
                .userId(userId)
                .modelId(modelId)
                .conversionId(conversionId)
                .role(role)
                .message(message)
                .files(files)
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .build();
    }
}