package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Qwen 文生图请求 DTO
 */
@Data
public class QwenTextToImageDTO {

    /**
     * 模型名称，固定为 qwen/text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "qwen-text-to-image", message = "Model must be qwen-text-to-image")
    private String model;

    /**
     * 图像生成提示词，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 图像尺寸
     */
    @Pattern(regexp = "square|square_hd|portrait_4_3|portrait_16_9|landscape_4_3|landscape_16_9",
            message = "Image size must be one of: square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9")
    private String imageSize;

    /**
     * 推理步数，2-250
     */
    @Min(value = 2, message = "Num inference steps must be >= 2")
    @Max(value = 250, message = "Num inference steps must be <= 250")
    private Integer numInferenceSteps;

    /**
     * 随机种子，用于复现结果
     */
    private Integer seed;

    /**
     * 引导比例，0-20
     */
    @Min(value = 0, message = "Guidance scale must be >= 0")
    @Max(value = 20, message = "Guidance scale must be <= 20")
    private Double guidanceScale;

    /**
     * 是否启用安全过滤器
     */
    private Boolean enableSafetyChecker;

    /**
     * 输出格式：png 或 jpeg
     */
    @Pattern(regexp = "png|jpeg", message = "Output format must be png or jpeg")
    private String outputFormat;

    /**
     * 负向提示词，最大500字符
     */
    @Size(max = 500, message = "Negative prompt cannot exceed 500 characters")
    private String negativePrompt;

    /**
     * 加速级别：none, regular, high
     */
    @Pattern(regexp = "none|regular|high", message = "Acceleration must be none, regular, or high")
    private String acceleration;
}