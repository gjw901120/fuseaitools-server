package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Hailuo 图生视频标准版请求 DTO
 * 模型示例: hailuo-2-3-image-to-video-standard
 */
@Data
public class HailuoImageToVideoStandardDTO {

    /**
     * 模型名称，例如 hailuo-2-3-image-to-video-standard
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 视频生成提示词，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 输入图片URL，格式为JPEG/PNG/WEBP，最大10MB
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 视频时长：6秒或10秒（10秒不支持1080P分辨率）
     */
    @Pattern(regexp = "6|10", message = "Duration must be 6 or 10")
    private String duration;

    /**
     * 视频分辨率：768P 或 1080P
     */
    @Pattern(regexp = "768P|1080P", message = "Resolution must be 768P or 1080P")
    private String resolution;
}