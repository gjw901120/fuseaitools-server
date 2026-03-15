package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * GPT Image 1.5 文生图请求 DTO
 * 模型示例: gpt-image-1.5-text-to-image
 */
@Data
public class GptImageTextToImageDTO {

    /**
     * 模型名称，例如 gpt-image-1.5-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 图像生成提示词，最大3000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 3000, message = "Prompt cannot exceed 3000 characters")
    private String prompt;

    /**
     * 图像宽高比：1:1, 2:3, 3:2
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|2:3|3:2", message = "Aspect ratio must be one of: 1:1, 2:3, 3:2")
    private String aspectRatio;

    /**
     * 画质：medium（平衡）或 high（慢/细节）
     */
    @NotBlank(message = "Quality cannot be empty")
    @Pattern(regexp = "medium|high", message = "Quality must be medium or high")
    private String quality;
}