package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Kling AI头像标准版请求 DTO
 * 模型示例: kling-ai-avatar-standard
 */
@Data
public class KlingAIAvatarStandardDTO {

    /**
     * 模型名称，例如 kling-ai-avatar-standard
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
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;
}