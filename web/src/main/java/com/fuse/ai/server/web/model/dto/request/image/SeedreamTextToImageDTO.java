package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Seedream 文生图请求 DTO
 */
@Data
public class SeedreamTextToImageDTO {

    /**
     * 模型名称，固定为 seedream/5-lite-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedream-5-lite-text-to-image", message = "Model must be seedream-5-lite-text-to-image")
    private String model;

    /**
     * 图像生成提示词，最大2995字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2995, message = "Prompt cannot exceed 2995 characters")
    private String prompt;

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