package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * HappyHorse V1 视频编辑请求 DTO
 * 模型端点固定使用: happyhorse-video-edit
 */
@Data
public class HappyHorseV1VideoEditDTO {

    /**
     * 模型名称，必须使用 happyhorse-video-edit
     */
    @NotBlank(message = "Model cannot be empty")
    private String model = "happyhorse-video-edit";

    /**
     * 视频编辑指令，用自然语言描述"要怎么改"，必填，最大5000字符
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
     * 待编辑的视频URL，必须且只能1个
     */
    @NotBlank(message = "Video URL cannot be empty")
    private String videoUrl;

    /**
     * 可选参考图片URL，用于风格/局部替换参考，最多5张
     */
    @Size(max = 5, message = "Reference images cannot exceed 5 items")
    private List<String> referenceImage;

    /**
     * 输出视频分辨率
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution = "1080p";

    /**
     * 上传视频时长
     * 范围：3-15
     */
    @Min(value = 3, message = "Duration must be at least 3 seconds")
    @Max(value = 60, message = "Duration must not exceed 60 seconds")
    private Integer  duration ;

    /**
     * 音频处理策略
     * auto: 由模型自动处理
     * origin: 尽量保留原视频音频
     * 默认值：auto
     */
    @Pattern(regexp = "auto|origin", message = "Audio setting must be auto or origin")
    private String audioSetting = "auto";

    /**
     * 随机种子
     * 范围：0-2147483647
     */
    private Long seed;
}