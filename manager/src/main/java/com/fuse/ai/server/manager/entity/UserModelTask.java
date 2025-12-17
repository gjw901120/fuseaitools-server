package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@TableName(value = "user_model_task", autoResultMap = true)
@Accessors(chain = true)
public class UserModelTask {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String recordId;

    private Integer modelId;

    private Integer pricingId;

    @Builder.Default
    private Integer status = TaskStatusEnum.PROCESSING.getCode();

    private String failedReason;

    private String thirdTaskId;

    // JSON数组字段
    @TableField(value = "input_urls", typeHandler = JacksonTypeHandler.class)
    private List<String> inputUrls;

    @TableField(value = "output_urls", typeHandler = JacksonTypeHandler.class)
    private List<String> outputUrls;

    @TableField(value = "input_details", typeHandler = JacksonTypeHandler.class)
    private Object inputDetails;

    @TableField(value = "output_details", typeHandler = JacksonTypeHandler.class)
    private Object outputDetails;

    @TableField(value = "output_callback_details", typeHandler = JacksonTypeHandler.class)
    private Object outputCallbackDetails;

    @Builder.Default
    private Integer isDel = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 获取状态枚举
     */
    public TaskStatusEnum getStatusEnum() {
        return TaskStatusEnum.getByCode(this.status);
    }

    /**
     * 设置状态枚举
     */
    public void setStatusEnum(TaskStatusEnum statusEnum) {
        this.status = statusEnum != null ? statusEnum.getCode() : null;
    }

    /**
     * 创建包含全量参数的任务对象
     */
    public static UserModelTask create(Integer userId, String recordId, Integer modelId,
                                       Integer pricingId, TaskStatusEnum status, String failedReason,
                                       String thirdTaskId, List<String> inputUrls, List<String> outputUrls,
                                       Object inputDetails, Object outputDetails,
                                       Object outputCallbackDetails) {
        return UserModelTask.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .pricingId(pricingId)
                .status(status != null ? status.getCode() : TaskStatusEnum.PROCESSING.getCode())
                .failedReason(failedReason)
                .thirdTaskId(thirdTaskId)
                .inputUrls(inputUrls)
                .outputUrls(outputUrls)
                .inputDetails(inputDetails)
                .outputDetails(outputDetails)
                .outputCallbackDetails(outputCallbackDetails)
                .build();
    }

    /**
     * 简化的创建方法（默认进行中状态）
     */
    public static UserModelTask createSimple(Integer userId, String recordId, Integer modelId,
                                             Integer pricingId, String thirdTaskId) {
        return UserModelTask.builder()
                .userId(userId)
                .recordId(recordId)
                .modelId(modelId)
                .pricingId(pricingId)
                .status(TaskStatusEnum.PROCESSING.getCode())
                .thirdTaskId(thirdTaskId)
                .build();
    }

    /**
     * 更新为成功状态
     */
    public UserModelTask updateToSuccess(List<String> outputUrls, Object outputDetails) {
        this.status = TaskStatusEnum.SUCCESS.getCode();
        this.outputUrls = outputUrls;
        this.outputDetails = outputDetails;
        this.gmtModified = LocalDateTime.now();
        return this;
    }

    /**
     * 更新为失败状态
     */
    public UserModelTask updateToFailed(String failedReason) {
        this.status = TaskStatusEnum.FAILED.getCode();
        this.failedReason = failedReason;
        this.gmtModified = LocalDateTime.now();
        return this;
    }

    /**
     * 更新为进行中状态
     */
    public UserModelTask updateToProcessing() {
        this.status = TaskStatusEnum.PROCESSING.getCode();
        this.gmtModified = LocalDateTime.now();
        return this;
    }

    /**
     * 判断是否成功状态
     */
    public boolean isSuccess() {
        return TaskStatusEnum.SUCCESS.getCode().equals(this.status);
    }

    /**
     * 判断是否失败状态
     */
    public boolean isFailed() {
        return TaskStatusEnum.FAILED.getCode().equals(this.status);
    }

    /**
     * 判断是否进行中状态
     */
    public boolean isProcessing() {
        return TaskStatusEnum.PROCESSING.getCode().equals(this.status);
    }
}