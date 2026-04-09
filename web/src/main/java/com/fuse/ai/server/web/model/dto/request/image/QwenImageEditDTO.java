package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Qwen 图像编辑请求 DTO
 */
@Data
public class QwenImageEditDTO {

    /**
     * 模型名称，固定为 qwen/image-edit
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "qwen-image-edit", message = "Model must be qwen-image-edit")
    private String model;

    /**
     * 编辑提示词，最大2000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2000, message = "Prompt cannot exceed 2000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 待编辑图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 加速级别：none, regular (high可选但文档中建议none/regular)
     */
    @Pattern(regexp = "none|regular|high", message = "Acceleration must be none, regular, or high")
    private String acceleration;

    /**
     * 输出图像尺寸
     */
    @Pattern(regexp = "square|square_hd|portrait_4_3|portrait_16_9|landscape_4_3|landscape_16_9",
            message = "Image size must be one of: square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9")
    private String imageSize;

    /**
     * 推理步数，2-49
     */
    @Min(value = 2, message = "Num inference steps must be >= 2")
    @Max(value = 49, message = "Num inference steps must be <= 49")
    private Integer numInferenceSteps;

    /**
     * 随机种子
     */
    private Integer seed;

    /**
     * 引导比例，0-20，默认4
     */
    @Min(value = 0, message = "Guidance scale must be >= 0")
    @Max(value = 20, message = "Guidance scale must be <= 20")
    private Double guidanceScale;

    /**
     * 同步模式：true-等待生成并直接返回图片
     */
    private Boolean syncMode;

    /**
     * 生成图片数量：1-4
     */
    @Pattern(regexp = "1|2|3|4", message = "Num images must be 1, 2, 3, or 4")
    private String numImages;

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
}