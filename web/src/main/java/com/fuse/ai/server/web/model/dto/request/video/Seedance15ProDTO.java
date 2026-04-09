package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Seedance 1.5 Pro 文生图/图生视频请求 DTO
 * 模型: seedance-1.5-pro
 */
@Data
public class Seedance15ProDTO {

    /**
     * 模型名称，固定为 seedance-1.5-pro
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedance-1.5-pro", message = "Model must be seedance-1.5-pro")
    private String model;

    /**
     * 用于视频生成的文本提示词
     * 最小长度：3，最大长度：2500 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 3, max = 2500, message = "Prompt must be between 3 and 2500 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 用于图生视频的输入图片 URL
     * 支持 0-2 张图片
     */
    private List<String> inputUrls;

    /**
     * 视频画面比例配置
     * 可选值：1:1, 4:3, 3:4, 16:9, 9:16, 21:9
     * 默认值：1:1
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|4:3|3:4|16:9|9:16|21:9", message = "Aspect ratio must be one of: 1:1, 4:3, 3:4, 16:9, 9:16, 21:9")
    private String aspectRatio;

    /**
     * 视频分辨率
     * 可选值：480p, 720p, 1080p
     * 默认值：720p
     */
    @Pattern(regexp = "480p|720p|1080p", message = "Resolution must be 480p, 720p, or 1080p")
    private String resolution;

    /**
     * 视频时长（单位：秒）
     * 可选值：4, 8, 12
     * 默认值：8
     */
    @Pattern(regexp = "4|8|12", message = "Duration must be 4, 8, or 12")
    private String duration;

    /**
     * 锁定摄像机，实现静态拍摄
     * true: 锁定摄像机，实现静态拍摄
     * false: 允许动态摄像机移动
     * 默认值：false
     */
    private Boolean fixedLens;

    /**
     * 是否为视频生成音频
     * true: 生成带音频的视频（费用更高）
     * false: 生成无音频的视频
     * 默认值：false
     */
    private Boolean generateAudio;

    /**
     * 是否启用 NSFW 内容检测器
     * Playground 中默认启用，API 调用可根据需要配置
     * 默认值：false
     */
    private Boolean nsfwChecker = false;
}