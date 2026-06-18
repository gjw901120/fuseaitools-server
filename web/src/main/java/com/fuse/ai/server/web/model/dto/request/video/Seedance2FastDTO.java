package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Seedance 2.0 Fast 视频生成请求 DTO
 * 模型: seedance-2-fast
 */
@Data
public class Seedance2FastDTO {

    /**
     * 模型名称，固定为 seedance-2-fast
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "seedance-2-fast", message = "Model must be seedance-2-fast")
    private String model;

    /**
     * 用于视频生成的文本提示词
     * 最小长度：3，最大长度：20000 字符
     */
    @Size(min = 3, max = 20000, message = "Prompt must be between 3 and 20000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify"
    )
    private String prompt;

    /**
     * 首帧图片地址或者 asset://{assetId}
     * 例如: asset://asset-20260404242101-76djj
     */
    private String firstFrameUrl;

    /**
     * 尾帧图片地址或者 asset://{assetId}
     * 例如: asset://asset-20260404242101-76djj
     */
    private String lastFrameUrl;

    /**
     * 输入图像 URL 或者 asset://{assetId} 列表
     * 最多 9 张图片（与首尾帧张数之和）
     * 格式：jpeg、png、webp、bmp、tiff、gif
     * 宽高比：0.4-2.5
     * 尺寸：300-6000px
     * 大小：单张 < 30MB
     */
    @Size(max = 9, message = "Maximum 9 reference images allowed (including first and last frames)")
    private List<String> referenceImageUrls = new ArrayList<>();

    /**
     * 输入视频 URL 或者 asset://{assetId} 列表
     * 最多 3 个视频
     * 格式：mp4、mov
     * 分辨率：480p、720p
     * 时长：单个 2-15s，总时长不超过 15s
     * 宽高比：0.4-2.5
     * 尺寸：300-6000px
     * 总像素数：409600-927408
     * 大小：单个 < 50MB
     * 帧率：24-60 FPS
     */
    @Size(max = 3, message = "Maximum 3 reference videos allowed")
    private List<String> referenceVideoUrls = new ArrayList<>();

    /**
     * 输入音频 URL 或者 asset://{assetId} 列表
     * 最多 3 段音频
     * 格式：wav、mp3
     * 时长：单个 2-15s，总时长不超过 15s
     * 大小：单个 < 15MB
     */
    @Size(max = 3, message = "Maximum 3 reference audios allowed")
    private List<String> referenceAudioUrls = new ArrayList<>();

    /**
     * 是否返回视频最后一帧图片（已废弃）
     * 默认值：false
     */
    @Deprecated
    private Boolean returnLastFrame;

    /**
     * 是否生成与画面同步的音频
     * 默认值：true
     */
    private Boolean generateAudio;

    /**
     * 视频分辨率
     * 可选值：480p, 720p
     * 默认值：720p
     */
    @Pattern(regexp = "480p|720p", message = "Resolution must be 480p or 720p")
    private String resolution;

    /**
     * 视频画面比例配置
     * 可选值：1:1, 4:3, 3:4, 16:9, 9:16, 21:9, adaptive
     * 默认值：16:9
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|4:3|3:4|16:9|9:16|21:9|adaptive", 
             message = "Aspect ratio must be one of: 1:1, 4:3, 3:4, 16:9, 9:16, 21:9, adaptive")
    private String aspectRatio;

    /**
     * 视频时长（单位：秒）
     * 范围：4-15
     * 默认值：5
     */
    @Pattern(regexp = "[4-9]|1[0-5]", message = "Duration must be between 4 and 15 seconds")
    private String duration;

    private String uploadDuration; // 上传视频时长

    /**
     * 是否启用联网搜索
     * 必填字段
     */
    private Boolean webSearch;

    /**
     * 是否启用 NSFW 内容检测器
     * 默认值：false
     */
    private Boolean nsfwChecker = false;
}
