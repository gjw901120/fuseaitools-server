package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Kling 2.6 图生视频请求 DTO
 * 模型示例: kling-2.6-image-to-video
 */
@Data
public class Kling26ImageToVideoDTO {

    /**
     * 模型名称，例如 kling-2.6-image-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 视频生成提示词，最大2500字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 输入图片URL列表，至少一张，格式为JPEG/PNG/WEBP，最大10MB
     */
    @NotNull(message = "Image URLs cannot be null")
    @Size(min = 1, message = "At least one image URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL") String> imageUrls;

    /**
     * 是否包含声音
     */
    @NotNull(message = "Sound cannot be null")
    private Boolean sound;

    /**
     * 视频时长：5秒或10秒
     */
    @NotBlank(message = "Duration cannot be empty")
    @Pattern(regexp = "5|10", message = "Duration must be 5 or 10")
    private String duration;
}