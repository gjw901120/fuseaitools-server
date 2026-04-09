package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Grok Imagine 图像生成视频请求 DTO
 * 模型: grok-imagine/image-to-video
 */
@Data
public class GrokImagineImageToVideoDTO {

    /**
     * 模型名称，固定为 grok-imagine/image-to-video
     */
    @Pattern(regexp = "grok-imagine-image-to-video", message = "Model must be grok-imagine-image-to-video")
    private String model;

    /**
     * 外部图像 URL 列表作为视频生成的参考
     * 最多支持 7 个图像
     */
    private List<String> imageUrls;

    /**
     * 描述期望视频运动的文本提示
     * 最大长度：5000 字符
     */
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 指定影响运动风格和强度的生成模式
     * fun: 更有创意和趣味的解读
     * normal: 平衡方法，具有良好的运动质量
     * spicy: 更有活力和强烈的运动效果（外部图像不可用）
     * 默认值：normal
     */
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
    @Pattern(regexp = "480p|720p", message = "Resolution must be 480p or 720p")
    private String resolution;

    /**
     * 图像比例选择，仅适用于多图生成模式
     * 单图模式下视频宽高参考图片宽高
     * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
     * 默认值：16:9
     */
    @Pattern(regexp = "2:3|3:2|1:1|16:9|9:16",
            message = "Aspect ratio must be one of: 2:3, 3:2, 1:1, 16:9, 9:16")
    private String aspectRatio;
}