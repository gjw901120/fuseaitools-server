package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Runway视频续集生成请求参数
 */
@Data
public class RunwayExtendDTO {

    /**
     * 原始视频生成任务的唯一标识符
     * 必须是来自先前生成视频的有效任务ID
     */
    @NotBlank(message = "Task ID cannot be empty")
    @Size(min = 1, max = 100, message = "Task ID length must be between 1-100 characters")
    private String taskId;

    /**
     * 指导视频续集的描述性文本
     * 解释接下来应该发生什么动作、动态或发展
     * 要具体但保持与原始视频内容的一致性
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 1, max = 1000, message = "Prompt length must be between 1-1000 characters")
    private String prompt;

    /**
     * 视频分辨率
     * 可选值为720p或1080p
     */
    @NotBlank(message = "Quality cannot be empty")
    private String quality;

    /**
     * 视频水印文本内容
     * 空字符串表示不添加水印，非空字符串将在视频右下角显示指定的水印文本
     */
    @Size(max = 50, message = "Watermark text cannot exceed 50 characters")
    private String waterMark;

    /**
     * 参数校验方法 - 验证视频分辨率
     */
    public boolean validateQuality() {
        return "720p".equals(quality) || "1080p".equals(quality);
    }

    /**
     * 参数校验方法 - 验证水印文本
     */
    public boolean validateWatermark() {
        return waterMark == null || waterMark.length() <= 50;
    }
}