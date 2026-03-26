package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Kling 3.0 运动控制请求 DTO
 * 模型: kling-3.0-motion-control
 */
@Data
public class Kling30MotionControlDTO {

    /**
     * 模型名称，固定为 kling-3.0-motion-control
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "kling-3.0-motion-control", message = "Model must be kling-3.0-motion-control")
    private String model;

    /**
     * 文本提示词，用于引导生成动画内容
     * 可为空或 0-2500 字符
     */
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    private String prompt;

    /**
     * 输入图片URL，包含一个图片url
     */
    @NotEmpty(message = "Input urls cannot be empty")
    private List<String> inputUrls;

    /**
     * 输入视频URL，包含一个视频url
     */
    @NotEmpty(message = "Video urls cannot be empty")
    private List<String> videoUrls;

    /**
     * 输入视频时长，单位秒
     */
    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    /**
     * 视频质量模式
     * std: 标准模式 (720p)
     * pro: 专业模式 (1080p)
     */
    @Pattern(regexp = "std|pro", message = "Mode must be std or pro")
    private String mode;

    /**
     * 角色朝向参考来源
     * video: 参考视频 (推荐)
     * image: 参考图片
     * 默认值: video
     */
    @Pattern(regexp = "video|image", message = "Character orientation must be video or image")
    private String characterOrientation;

    /**
     * 背景来源
     * input_video: 使用视频背景
     * input_image: 使用图片背景
     * 默认值: input_video
     */
    @Pattern(regexp = "input_video|input_image", message = "Background source must be input_video or input_image")
    private String backgroundSource;
}