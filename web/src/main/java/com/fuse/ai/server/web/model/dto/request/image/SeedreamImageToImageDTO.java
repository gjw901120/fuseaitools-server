package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Seedream 图生图请求 DTO
 */
@Data
public class SeedreamImageToImageDTO {

    /**
     * 模型名称，固定为 seedream/5-lite-image-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedream-5-lite-image-to-image", message = "Model must be seedream-5-lite-image-to-image")
    private String model;

    /**
     * 图像编辑提示词，最大2996字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2996, message = "Prompt cannot exceed 2996 characters")
    private String prompt;

    /**
     * 输入图片URL列表，至少一张，格式为JPEG/PNG/WEBP，最大10MB
     */
    @NotNull(message = "Image URLs cannot be null")
    @Size(min = 1, message = "At least one image URL is required")
    private List<String> imageUrls;

    /**
     * 图像宽高比
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|4:3|3:4|16:9|9:16|2:3|3:2|21:9",
            message = "Aspect ratio must be one of: 1:1, 4:3, 3:4, 16:9, 9:16, 2:3, 3:2, 21:9")
    private String aspectRatio;

    /**
     * 画质：basic（2K）或 high（3K）
     */
    @NotBlank(message = "Quality cannot be empty")
    @Pattern(regexp = "basic|high", message = "Quality must be basic or high")
    private String quality;
}