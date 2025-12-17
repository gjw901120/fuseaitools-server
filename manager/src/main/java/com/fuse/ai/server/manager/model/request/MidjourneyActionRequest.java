package com.fuse.ai.server.manager.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Action 动作请求参数
 */
@Data
public class MidjourneyActionRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 是否选择同一频道下的账号
     */
    private Boolean chooseSameChannel = false;

    /**
     * 动作标识符
     */
    @NotBlank(message = "动作标识符不能为空")
    private String customId;

    /**
     * 要执行动作的任务 ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    private String notifyHook;

    private String state;

}