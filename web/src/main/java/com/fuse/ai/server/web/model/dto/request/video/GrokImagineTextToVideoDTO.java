package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Grok Imagine 文本生成视频请求 DTO
 * 模型: grok-imagine/text-to-video
 */
@Data
public class GrokImagineTextToVideoDTO {

    /**
     * 模型名称，固定为 grok-imagine/text-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "grok-imagine-text-to-video", message = "Model must be grok-imagine-text-to-video")
    private String model;

    /**
     * 描述期望视频运动的文本提示
     * 最大长度：5000 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 指定生成视频的宽高比
     * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
     * 默认值：2:3
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "2:3|3:2|1:1|16:9|9:16",
            message = "Aspect ratio must be one of: 2:3, 3:2, 1:1, 16:9, 9:16")
    private String aspectRatio;

    /**
     * 指定影响运动风格和强度的生成模式
     * fun: 更有创意和趣味的解读
     * normal: 平衡方法，具有良好的运动质量
     * spicy: 更有活力和强烈的运动效果
     * 默认值：normal
     */
    @NotBlank(message = "Mode cannot be empty")
    @Pattern(regexp = "fun|normal|spicy", message = "Mode must be fun, normal, or spicy")
    private String mode;

    /**
     * 生成的视频时长（秒）
     * 最小值：6，最大值：30，步长：1
     */
    private BigDecimal duration;

    /**
     * 生成视频的分辨率
     * 可选值：480p, 720p
     * 默认值：480p
     */
    @NotBlank(message = "Resolution cannot be empty")
    @Pattern(regexp = "480p|720p", message = "Resolution must be 480p or 720p")
    private String resolution;
}