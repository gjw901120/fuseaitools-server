package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Flux 2 文本生成图像请求 DTO
 * 模型: flux-2-text-to-image
 */
@Data
public class Flux2TextToImageDTO {

    /**
     * 模型名称，固定为 flux-2-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "flux-2-text-to-image", message = "Model must be flux-2-text-to-image")
    private String model;

    /**
     * 文本提示词
     * 长度必须在 3-5000 字符之间
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 3, max = 5000, message = "Prompt must be between 3 and 5000 characters")
    private String prompt;

    /**
     * 生成图像的宽高比
     * 可选值：1:1, 4:3, 3:4, 16:9, 9:16, 3:2, 2:3, auto
     * 默认值：1:1
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|4:3|3:4|16:9|9:16|3:2|2:3|auto", message = "Aspect ratio must be one of: 1:1, 4:3, 3:4, 16:9, 9:16, 3:2, 2:3, auto")
    private String aspectRatio;

    /**
     * 输出图像分辨率
     * 可选值：1K, 2K
     * 默认值：1K
     */
    @NotBlank(message = "Resolution cannot be empty")
    @Pattern(regexp = "1K|2K", message = "Resolution must be 1K or 2K")
    private String resolution;

    /**
     * NSFW 内容检测器
     * Playground 中默认启用，API 调用可根据需要配置
     */
    private Boolean nsfwChecker = false;
}