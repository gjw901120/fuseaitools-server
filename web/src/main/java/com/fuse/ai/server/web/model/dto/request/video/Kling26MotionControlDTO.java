package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Kling 2.6 运动控制请求 DTO
 * 模型示例: kling-2.6-motion-control
 */
@Data
public class Kling26MotionControlDTO {

    /**
     * 模型名称，例如 kling-2.6-motion-control
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 提示词，最大2500字符
     */
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 参考图片URL列表
     */
    @NotNull(message = "Input URLs cannot be null")
    @Size(min = 1, max = 1, message = "At least one input URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Input URL must be a valid URL") String> inputUrls;

    /**
     * 参考视频URL列表
     */
    @NotNull(message = "Video URLs cannot be null")
    @Size(min = 1, max = 1, message = "At least one video URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Video URL must be a valid URL") String> videoUrls;

    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    /**
     * 角色朝向：image 或 video
     */
    @NotBlank(message = "Character orientation cannot be empty")
    @Pattern(regexp = "image|video", message = "Character orientation must be image or video")
    private String characterOrientation;

    /**
     * 输出分辨率模式：720p 或 1080p
     */
    @NotBlank(message = "Mode cannot be empty")
    @Pattern(regexp = "720p|1080p", message = "Mode must be 720p or 1080p")
    private String mode;
}