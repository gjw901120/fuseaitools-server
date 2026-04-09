package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Wan 图生视频请求 DTO
 */
@Data
public class WanImageToVideoDTO {

    /**
     * 模型名称，固定为 wan/2-6-image-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-6-image-to-video", message = "Model must be wan-2-6-image-to-video")
    private String model;

    /**
     * 视频生成提示词，1-5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 图片URL列表，至少一张，格式为JPEG/PNG/WEBP，最大10MB，最小256x256px
     */
    @NotNull(message = "Image URLs cannot be null")
    @Size(min = 1, message = "At least one image URL is required")
    private List<String> imageUrls;

    /**
     * 视频时长：5秒、10秒、15秒
     */
    @Pattern(regexp = "5|10|15", message = "Duration must be 5, 10, or 15")
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