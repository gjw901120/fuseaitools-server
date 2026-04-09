package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Qwen 图生图请求 DTO
 */
@Data
public class QwenImageToImageDTO {

    /**
     * 模型名称，固定为 qwen/image-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "qwen-image-to-image", message = "Model must be qwen-image-to-image")
    private String model;

    /**
     * 图像生成提示词，最大5000字符
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
     * 参考图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 去噪强度，0-1
     */
    @Min(value = 0, message = "Strength must be >= 0")
    @Max(value = 1, message = "Strength must be <= 1")
    private Double strength;

    /**
     * 输出格式：png 或 jpeg
     */
    @Pattern(regexp = "png|jpeg", message = "Output format must be png or jpeg")
    private String outputFormat;

    /**
     * 加速级别：none, regular, high
     */
    @Pattern(regexp = "none|regular|high", message = "Acceleration must be none, regular, or high")
    private String acceleration;

    /**
     * 负向提示词，最大500字符
     */
    @Size(max = 500, message = "Negative prompt cannot exceed 500 characters")
    private String negativePrompt;

    /**
     * 随机种子
     */
    private Integer seed;

    /**
     * 推理步数，2-250
     */
    @Min(value = 2, message = "Num inference steps must be >= 2")
    @Max(value = 250, message = "Num inference steps must be <= 250")
    private Integer numInferenceSteps;

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
}