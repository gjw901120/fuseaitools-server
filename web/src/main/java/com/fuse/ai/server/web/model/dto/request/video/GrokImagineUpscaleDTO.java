package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Grok Imagine 视频放大请求 DTO
 * 模型: grok-imagine/upscale
 */
@Data
public class GrokImagineUpscaleDTO {

    /**
     * 模型名称，固定为 grok-imagine/upscale
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "grok-imagine-upscale", message = "Model must be grok-imagine-upscale")
    private String model;


    /**
     * 之前成功的图像生成任务的任务 ID
     * 必须来自 Kie AI 图像生成模型（例如 grok-imagine/text-to-image）
     * 最大长度：100 字符
     */
    @NotBlank(message = "Task ID cannot be empty")
    @Size(max = 100, message = "Task ID cannot exceed 100 characters")
    private String taskId;
}