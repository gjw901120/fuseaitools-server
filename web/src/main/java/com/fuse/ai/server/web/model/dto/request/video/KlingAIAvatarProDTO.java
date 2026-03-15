package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Kling AI头像专业版请求 DTO
 * 模型示例: kling-ai-avatar-pro
 */
@Data
public class KlingAIAvatarProDTO {

    /**
     * 模型名称，例如 kling-ai-avatar-pro
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 头像图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 音频文件URL
     */
    @NotBlank(message = "Audio URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Audio URL must be a valid URL")
    private String audioUrl;

    /**
     * 提示词，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;
}