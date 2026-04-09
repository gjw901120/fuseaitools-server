package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Ideogram 角色生成请求 DTO
 * 模型示例: ideogram-character
 */
@Data
public class IdeogramCharacterDTO {

    /**
     * 模型名称，例如 ideogram-character
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

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
     * 生成图片数量：1, 2, 3, 4
     */
    @Pattern(regexp = "1|2|3|4", message = "Number of images must be 1, 2, 3, or 4")
    private String numImages;

    /**
     * 图片尺寸：square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
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