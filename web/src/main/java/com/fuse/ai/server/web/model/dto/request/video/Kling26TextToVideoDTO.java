package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Kling 2.6 文生视频请求 DTO
 * 模型示例: kling-2.6-text-to-video
 */
@Data
public class Kling26TextToVideoDTO {

    /**
     * 模型名称，例如 kling-2.6-text-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 视频生成提示词，最大2500字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    private String prompt;

    /**
     * 是否包含声音
     */
    @NotNull(message = "Sound cannot be null")
    private Boolean sound;

    /**
     * 视频宽高比：1:1, 16:9, 9:16
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|16:9|9:16", message = "Aspect ratio must be 1:1, 16:9, or 9:16")
    private String aspectRatio;

    /**
     * 视频时长：5秒或10秒
     */
    @NotBlank(message = "Duration cannot be empty")
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;
}