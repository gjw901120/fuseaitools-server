package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Ideogram V3 重构请求 DTO
 * 模型示例: ideogram-v3-reframe
 */
@Data
public class IdeogramV3ReframeDTO {

    /**
     * 模型名称，例如 ideogram-v3-reframe
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    @Pattern(regexp = "^(http|https)://.*$", message = "Callback URL must be a valid URL")
    private String callBackUrl;

    /**
     * 输入图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 输出图片尺寸：square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
     */
    @NotBlank(message = "Image size cannot be empty")
    @Pattern(regexp = "square|square_hd|portrait_4_3|portrait_16_9|landscape_4_3|landscape_16_9",
            message = "Image size must be one of: square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9")
    private String imageSize;

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
     * 生成图片数量：1, 2, 3, 4
     */
    @Pattern(regexp = "1|2|3|4", message = "Number of images must be 1, 2, 3, or 4")
    private String numImages;

    /**
     * 随机数种子
     */
    private Integer seed;
}