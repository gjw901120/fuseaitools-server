package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Ideogram V3 文生图请求 DTO
 * 模型示例: ideogram-v3-text-to-image
 */
@Data
public class IdeogramV3TextToImageDTO {

    /**
     * 模型名称，例如 ideogram-v3-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 图像生成提示词，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 渲染速度：TURBO, BALANCED, QUALITY
     */
    @Pattern(regexp = "TURBO|BALANCED|QUALITY", message = "Rendering speed must be TURBO, BALANCED, or QUALITY")
    private String renderingSpeed;

    /**
     * 风格：AUTO, GENERAL, REALISTIC, DESIGN
     */
    @Pattern(regexp = "AUTO|GENERAL|REALISTIC|DESIGN", message = "Style must be AUTO, GENERAL, REALISTIC, or DESIGN")
    private String style;

    /**
     * 是否使用MagicPrompt扩展提示词
     */
    private Boolean expandPrompt;

    /**
     * 图片尺寸：square, squareHd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
     */
    @Pattern(regexp = "square|square_hd|portrait_4_3|portrait_16_9|landscape_4_3|landscape_16_9",
            message = "Image size must be one of: square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9")
    private String imageSize;

    /**
     * 随机数种子
     */
    private Integer seed;

    /**
     * 负面提示词，最大5000字符
     */
    @Size(max = 5000, message = "Negative prompt cannot exceed 5000 characters")
    private String negativePrompt;
}