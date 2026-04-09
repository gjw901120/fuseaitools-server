package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Kling Turbo 图生视频专业版请求 DTO
 * 模型示例: kling-v2-5-turbo-image-to-video-pro
 */
@Data
public class KlingTurboImageToVideoProDTO {

    /**
     * 模型名称，例如 kling-v2-5-turbo-image-to-video-pro
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 视频生成提示词，最大2500字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 输入图片URL，格式为JPEG/PNG/WEBP，最大10MB
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 尾帧图片URL
     */
    @Pattern(regexp = "^(http|https)://.*$", message = "Tail image URL must be a valid URL")
    private String tailImageUrl;

    /**
     * 视频时长：5秒或10秒
     */
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;

    /**
     * 负面提示词，避免的内容，最大2496字符
     */
    @Size(max = 2496, message = "Negative prompt cannot exceed 2496 characters")
    private String negativePrompt;

    /**
     * CFG引导尺度，范围0-1，步长0.1
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "CFG scale must be between 0 and 1")
    @DecimalMax(value = "1.0", inclusive = true, message = "CFG scale must be between 0 and 1")
    private BigDecimal cfgScale;
}