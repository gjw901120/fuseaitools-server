package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * HappyHorse V1 文生视频请求 DTO
 * 模型端点固定使用: happyhorse-text-to-video
 */
@Data
public class HappyHorseV1TextToVideoDTO {

    /**
     * 模型名称，必须使用 happyhorse-text-to-video
     */
    @NotBlank(message = "Model cannot be empty")
    private String model = "happyhorse-text-to-video";

    /**
     * 用于描述要生成的视频内容、场景与风格的文本提示词，必填，最大5000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,
            message = "Contains inappropriate content. Please modify"
    )
    private String prompt;

    /**
     * 输出视频分辨率
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution = "1080p";

    /**
     * 输出视频的宽高比（画面比例）
     * 可选值：16:9, 9:16, 1:1, 4:3, 3:4
     * 默认值：16:9
     */
    @Pattern(regexp = "16:9|9:16|1:1|4:3|3:4", message = "Aspect ratio must be one of: 16:9, 9:16, 1:1, 4:3, 3:4")
    private String aspectRatio = "16:9";

    /**
     * 输出视频时长（单位：秒）
     * 范围：3-15
     * 默认值：5
     */
    @Pattern(regexp = "[3-9]|1[0-5]", message = "Duration must be between 3 and 15 seconds")
    private String duration = "5";

    /**
     * 随机种子，用于可复现/控制随机性
     * 范围：0-2147483647
     */
    private Long seed;
}