package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Ideogram 角色重混请求 DTO
 * 模型示例: ideogram-character-remix
 */
@Data
public class IdeogramCharacterRemixDTO {

    /**
     * 模型名称，例如 ideogram-character-remix
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

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
     * 角色参考图片URL列表，目前仅支持1张
     */
    @NotNull(message = "Reference image URLs cannot be null")
    @Size(min = 1, max = 1, message = "Exactly one reference image URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL") String> referenceImageUrls;

    /**
     * 渲染速度：TURBO, BALANCED, QUALITY
     */
    @Pattern(regexp = "TURBO|BALANCED|QUALITY", message = "Rendering speed must be TURBO, BALANCED, or QUALITY")
    private String renderingSpeed;

    /**
     * 风格：AUTO, REALISTIC, FICTION
     */
    @Pattern(regexp = "AUTO|REALISTIC|FICTION", message = "Style must be AUTO, REALISTIC, or FICTION")
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
     * 输入图片强度，范围0.1-1，步长0.1，默认0.8
     */
    @DecimalMin(value = "0.1", message = "Strength must be at least 0.1")
    @DecimalMax(value = "1.0", message = "Strength must be at most 1.0")
    private BigDecimal strength;

    /**
     * 负面提示词，最大500字符
     */
    @Size(max = 500, message = "Negative prompt cannot exceed 500 characters")
    private String negativePrompt;

    /**
     * 风格参考图片URL列表
     */
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL") String> imageUrls;

    /**
     * 参考遮罩URL，目前仅支持1张
     */
    @Pattern(regexp = "^(http|https)://.*$", message = "Reference mask URL must be a valid URL")
    private String referenceMaskUrls;
}