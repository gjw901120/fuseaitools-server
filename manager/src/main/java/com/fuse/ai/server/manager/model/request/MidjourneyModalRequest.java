package com.fuse.ai.server.manager.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Modal 任务请求参数
 */
@Data
public class MidjourneyModalRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 局部重绘的蒙版图片 base64 编码
     */
    private String maskBase64;

    /**
     * 重绘区域的文本提示词
     */
    private String prompt;

    /**
     * 原始任务的 ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 构建 Modal 请求
     */
    public static MidjourneyModalRequest build(String taskId, String prompt, String maskBase64) {
        MidjourneyModalRequest request = new MidjourneyModalRequest();
        request.setTaskId(taskId);
        request.setPrompt(prompt);
        request.setMaskBase64(maskBase64);
        return request;
    }
}