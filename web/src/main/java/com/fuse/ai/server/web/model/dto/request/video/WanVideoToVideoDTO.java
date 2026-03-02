package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Wan 视频生视频请求 DTO
 */
@Data
public class WanVideoToVideoDTO {

    /**
     * 模型名称，固定为 wan/2-6-video-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-6-video-to-video", message = "Model must be wan-2-6-video-to-video")
    private String model;

    /**
     * 视频生成提示词，1-5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 视频URL列表，至少一个，格式为MP4/MOV/MKV，最大10MB
     */
    @NotNull(message = "Video URLs cannot be null")
    @Size(min = 1, message = "At least one video URL is required")
    private List<String> videoUrls;

    /**
     * 视频时长：5秒或10秒（注意：视频生视频不支持15秒）
     */
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;

    /**
     * 分辨率：720p 或 1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution;

    /**
     * 是否多镜头：true=多镜头，false=单镜头
     */
    private Boolean multiShots;
}