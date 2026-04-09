package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Seedance Pro Fast 图生视频请求 DTO
 */
@Data
public class SeedanceProFastImageToVideoDTO {

    /**
     * 模型名称，固定为 bytedance/v1-pro-fast-image-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedance-v1-pro-fast-image-to-video", message = "Model must be seedance-v1-pro-fast-image-to-video")
    private String model;

    /**
     * 视频生成提示词，最大10000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 10000, message = "Prompt cannot exceed 10000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 输入图片URL，JPEG/PNG/WEBP，最大10MB
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 分辨率：720p 或 1080p（注意：Pro Fast 不支持480p）
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution;

    /**
     * 视频时长：5秒或10秒
     */
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;
}