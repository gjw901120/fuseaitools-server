package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * HappyHorse V1 图生视频请求 DTO
 * 模型端点固定使用: happyhorse-image-to-video
 */
@Data
public class HappyHorseV1ImageToVideoDTO {

    /**
     * 模型名称，必须使用 happyhorse-image-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    private String model = "happyhorse-image-to-video";

    /**
     * 可选的文本提示词，用于补充/约束由首帧图驱动的视频内容，最大5000字符
     */
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,
            message = "Contains inappropriate content. Please modify"
    )
    private String prompt;

    /**
     * 输入首帧图片URL，必须且只能1张
     */
    @NotEmpty(message = "Image URLs cannot be empty")
    @Size(max = 1, message = "Exactly 1 image URL is required")
    private List<String> imageUrls;

    /**
     * 输出视频分辨率
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution = "1080p";

    /**
     * 输出视频时长（单位：秒）
     * 范围：3-15
     * 默认值：5
     */
    @Pattern(regexp = "[3-9]|1[0-5]", message = "Duration must be between 3 and 15 seconds")
    private String duration = "5";

    /**
     * 随机种子
     * 范围：0-2147483647
     */
    private Long seed;
}