package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Seedance Lite 文生视频请求 DTO
 */
@Data
public class SeedanceLiteTextToVideoDTO {

    /**
     * 模型名称，固定为 bytedance/v1-lite-text-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedance-v1-lite-text-to-video", message = "Model must be seedance-v1-lite-text-to-video")
    private String model;

    /**
     * 视频生成提示词，最大10000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 10000, message = "Prompt cannot exceed 10000 characters")
    private String prompt;

    /**
     * 视频宽高比
     */
    @Pattern(regexp = "16:9|4:3|1:1|3:4|9:16|9:21", message = "Aspect ratio must be one of: 16:9, 4:3, 1:1, 3:4, 9:16, 9:21")
    private String aspectRatio;

    /**
     * 分辨率：480p, 720p, 1080p
     */
    @Pattern(regexp = "480p|720p|1080p", message = "Resolution must be 480p, 720p, or 1080p")
    private String resolution;

    /**
     * 视频时长：5秒或10秒
     */
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;

    /**
     * 是否固定相机
     */
    private Boolean cameraFixed;

    /**
     * 随机种子，-1表示随机
     */
    @Min(value = -1, message = "Seed must be >= -1")
    @Max(value = 2147483647, message = "Seed must be <= 2147483647")
    private Integer seed;

    /**
     * 是否启用安全过滤器
     */
    private Boolean enableSafetyChecker;
}