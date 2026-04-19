package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Wan 2.7 视频编辑请求 DTO
 * 模型: wan-2-7-videoedit
 */
@Data
public class Wan27VideoEditDTO {

    /**
     * 模型名称，固定为 wan-2-7-videoedit
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-7-videoedit", message = "Model must be wan-2-7-videoedit")
    private String model;

    /**
     * 文本提示词，用来描述生成视频中期望包含的元素和视觉特点
     * 支持中英文，最大长度 5000 字符
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
     * 反向提示词，用来描述不希望出现在视频画面中的内容
     * 支持中英文，最大长度 500 字符
     */
    @Size(max = 500, message = "Negative prompt must not exceed 500 characters")
    private String negativePrompt;

    /**
     * 待编辑视频的 URL（必填）
     * 格式：mp4、mov
     * 时长：2-10 秒
     * 分辨率：宽高范围 [240,4096] 像素
     * 宽高比：1:8 到 8:1
     * 文件大小：不超过 100MB
     */
    @NotBlank(message = "Video URL cannot be empty")
    private String videoUrl;

    /**
     * 参考图像 URL，用于人物、服饰、风格等参考
     * 格式：JPEG、JPG、PNG（不支持透明通道）、BMP、WEBP
     * 分辨率：宽高范围 [240,8000] 像素
     * 宽高比：1:8 到 8:1
     * 文件大小：不超过 20MB
     */
    private String referenceImage;

    /**
     * 输出视频分辨率档位
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution;

    /**
     * 输出视频宽高比
     * 不传时：默认按输入视频宽高比生成近似比例视频
     * 传入时：按指定宽高比生成
     * 可选值：16:9, 9:16, 1:1, 4:3, 3:4
     */
    @Pattern(regexp = "16:9|9:16|1:1|4:3|3:4", message = "Aspect ratio must be one of: 16:9, 9:16, 1:1, 4:3, 3:4")
    private String aspectRatio;

    /**
     * 输出视频时长（单位：秒）
     * 默认值为 0，表示直接使用输入视频时长，不截断
     * 传入指定值时，从原视频 0 秒开始截取至该长度
     * 合法取值为 0 或 [2,10] 之间的整数
     */
    @Pattern(regexp = "0|[2-9]|10", message = "Duration must be 0 or between 2 and 10 seconds")
    private String duration;

    /**
     * 视频声音设置
     * auto：默认，模型根据 prompt 智能判断是否重生成音频
     * origin：强制保留输入视频原声
     * 默认值：auto
     */
    @Pattern(regexp = "auto|origin", message = "Audio setting must be auto or origin")
    private String audioSetting;

    /**
     * 是否开启 prompt 智能改写
     * 开启后使用大模型对输入 prompt 进行扩写，短 prompt 场景下效果更好，但会增加耗时
     * 默认值：true
     */
    private Boolean promptExtend;

    /**
     * 是否添加水印
     * 水印位于视频右下角，文案固定为"AI生成"
     * 默认值：false
     */
    private Boolean watermark;

    /**
     * 随机种子
     * 范围：0-2147483647
     * 未传时系统自动生成
     */
    @Pattern(regexp = "\\d+", message = "Seed must be a non-negative integer")
    private String seed;

    /**
     * 是否启用 NSFW 内容检测器
     * 默认值：false
     */
    private Boolean nsfwChecker = false;
}
