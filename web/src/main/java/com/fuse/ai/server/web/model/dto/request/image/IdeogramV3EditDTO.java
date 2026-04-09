package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Ideogram V3 编辑请求 DTO
 * 模型示例: ideogram-v3-edit
 */
@Data
public class IdeogramV3EditDTO {

    /**
     * 模型名称，例如 ideogram-v3-edit
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 提示词，用于填充遮罩部分，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 输入图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 遮罩图片URL，需与输入图片尺寸匹配
     */
    @NotBlank(message = "Mask URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Mask URL must be a valid URL")
    private String maskUrl;

    /**
     * 渲染速度：TURBO, BALANCED, QUALITY
     */
    @Pattern(regexp = "TURBO|BALANCED|QUALITY", message = "Rendering speed must be TURBO, BALANCED, or QUALITY")
    private String renderingSpeed;

    /**
     * 是否使用MagicPrompt扩展提示词
     */
    private Boolean expandPrompt;

    /**
     * 随机数种子
     */
    private Integer seed;
}