package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Wan 2.7 文生视频请求 DTO
 * 模型: wan-2-7-text-to-video
 */
@Data
public class Wan27TextToVideoDTO {

    /**
     * 模型名称，固定为 wan-2-7-text-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-7-text-to-video", message = "Model must be wan-2-7-text-to-video")
    private String model;

    /**
     * 正向提示词
     * 最少 1 个字符，最多 5000 个字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 3, max = 5000, message = "Prompt must be between 1 and 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify"
    )
    private String prompt;

    /**
     * 反向提示词
     * 最多 500 个字符
     */
    @Size(max = 500, message = "Negative prompt must not exceed 500 characters")
    private String negativePrompt;

    /**
     * 可选的自定义音频 URL
     */
    private String audioUrl;

    /**
     * 视频分辨率
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution;

    /**
     * 视频宽高比
     * 可选值：16:9, 9:16, 1:1, 4:3, 3:4
     * 默认值：16:9
     */
    @Pattern(regexp = "16:9|9:16|1:1|4:3|3:4", message = "Ratio must be one of: 16:9, 9:16, 1:1, 4:3, 3:4")
    private String ratio;

    /**
     * 视频时长（单位：秒）
     * 范围：2-15
     * 默认值：5
     */
    @Pattern(regexp = "[2-9]|1[0-5]", message = "Duration must be between 2 and 15 seconds")
    private String duration;

    /**
     * 是否开启提示词智能改写
     * 默认值：true
     */
    private Boolean promptExtend;

    /**
     * 是否添加 AI 生成水印
     * 默认值：false
     */
    private Boolean watermark;

    /**
     * 随机种子
     * 范围：0-2147483647
     */
    @Pattern(regexp = "\\d+", message = "Seed must be a non-negative integer")
    private String seed;

    /**
     * 是否启用 NSFW 内容检测器
     * 默认值：false
     */
    private Boolean nsfwChecker = false;
}
