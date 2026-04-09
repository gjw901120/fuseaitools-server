package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Kling Turbo 文生视频专业版请求 DTO
 * 模型示例: kling-v2-5-turbo-text-to-video-pro
 */
@Data
public class KlingTurboTextToVideoProDTO {

    /**
     * 模型名称，例如 kling-v2-5-turbo-text-to-video-pro
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
     * 视频时长：5秒或10秒
     */
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;

    /**
     * 视频宽高比：16:9, 9:16, 1:1
     */
    @Pattern(regexp = "16:9|9:16|1:1", message = "Aspect ratio must be 16:9, 9:16, or 1:1")
    private String aspectRatio;

    /**
     * 负面提示词，避免的内容，最大2500字符
     */
    @Size(max = 2500, message = "Negative prompt cannot exceed 2500 characters")
    private String negativePrompt;

    /**
     * CFG引导尺度，范围0-1，步长0.1
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "CFG scale must be between 0 and 1")
    @DecimalMax(value = "1.0", inclusive = true, message = "CFG scale must be between 0 and 1")
    private BigDecimal cfgScale;
}