package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Wan 2.7 R2V (Reference to Video) 请求 DTO
 * 模型: wan-2-7-r2v
 */
@Data
public class Wan27R2vDTO {

    /**
     * 模型名称，固定为 wan-2-7-r2v
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-7-r2v", message = "Model must be wan-2-7-r2v")
    private String model;

    /**
     * 文本提示词（必填）
     * 用来描述生成视频中期望包含的元素和视觉特点
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
     * 反向提示词
     * 用来描述不希望在视频画面中出现的内容
     * 支持中英文，最大长度 500 字符
     */
    @Size(max = 500, message = "Negative prompt must not exceed 500 characters")
    private String negativePrompt;

    /**
     * 参考图像 URL 数组
     * 参考图像与参考视频至少传入一种
     * 图像数 + 视频数总和不能超过 5
     */
    @Size(max = 5, message = "Maximum 5 reference images allowed")
    private List<String> referenceImage;

    /**
     * 参考视频 URL 数组
     * 参考图像与参考视频至少传入一种
     * 图像数 + 视频数总和不能超过 5
     */
    @Size(max = 5, message = "Maximum 5 reference videos allowed")
    private List<String> referenceVideo;

    /**
     * 首帧图像 URL，最多传入 1 张
     * 传入后会自动忽略 aspect_ratio，以首帧图像宽高比生成近似比例视频
     */
    private String firstFrame;

    /**
     * 音频 URL，用于指定参考素材主体角色的音色
     * 格式：wav、mp3
     * 时长：1-10 秒
     * 文件大小：不超过 15MB
     * 
     * 规则：
     * - 若 reference_video 本身有音频但未传 reference_voice，默认使用视频原声
     * - 若同时传入 reference_video 和 reference_voice，则优先使用 reference_voice
     */
    private String referenceVoice;

    /**
     * 输出视频分辨率档位
     * 可选值：720p, 1080p
     * 默认值：1080p
     */
    @Pattern(regexp = "720p|1080p", message = "Resolution must be 720p or 1080p")
    private String resolution;

    /**
     * 输出视频宽高比
     * 生效逻辑：
     * - 未传入 first_frame：按指定的 aspect_ratio 生成视频
     * - 已传入 first_frame：自动忽略 aspect_ratio，以首帧图像宽高比生成近似比例视频
     * 可选值：16:9, 9:16, 1:1, 4:3, 3:4
     * 默认值：16:9
     */
    @Pattern(regexp = "16:9|9:16|1:1|4:3|3:4", message = "Aspect ratio must be one of: 16:9, 9:16, 1:1, 4:3, 3:4")
    private String aspectRatio;

    /**
     * 输出视频时长（单位：秒）
     * 取值范围：2-10 的整数
     * 默认值：5
     */
    @Pattern(regexp = "[2-9]|10", message = "Duration must be between 2 and 10 seconds")
    private String duration;

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
