package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Ideogram V3 重混请求 DTO
 * 模型示例: ideogram-v3-remix
 */
@Data
public class IdeogramV3RemixDTO {

    /**
     * 模型名称，例如 ideogram-v3-remix
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    @Pattern(regexp = "^(http|https)://.*$", message = "Callback URL must be a valid URL")
    private String callBackUrl;

    /**
     * 提示词，用于重混图片，最大5000字符
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
     * 图片尺寸：square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
     */
    @Pattern(regexp = "square|square_hd|portrait_4_3|portrait_16_9|landscape_4_3|landscape_16_9",
            message = "Image size must be one of: square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9")
    private String imageSize;

    /**
     * 生成图片数量：1, 2, 3, 4
     */
    @Pattern(regexp = "1|2|3|4", message = "Number of images must be 1, 2, 3, or 4")
    private String numImages;

    /**
     * 随机数种子
     */
    private Integer seed;

    /**
     * 输入图片强度，范围0.01-1，步长0.01
     */
    @DecimalMin(value = "0.01", message = "Strength must be at least 0.01")
    @DecimalMax(value = "1.0", message = "Strength must be at most 1.0")
    private BigDecimal strength;

    /**
     * 负面提示词，最大5000字符
     */
    @Size(max = 5000, message = "Negative prompt cannot exceed 5000 characters")
    private String negativePrompt;
}