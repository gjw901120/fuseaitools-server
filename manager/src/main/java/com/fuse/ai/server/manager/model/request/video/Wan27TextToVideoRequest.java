package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通义万相2.7 (Wan2.7) 文生视频请求
 * 模型端点固定使用: wan/2-7-text-to-video
 *
 * 支持功能：
 * - 视频时长：2-15秒（默认5秒）
 * - 分辨率：720p、1080p（默认1080p）
 * - 宽高比：16:9、9:16、1:1、4:3、3:4（默认16:9）
 * - 反向提示词、自定义音频、提示词智能改写
 * - 水印控制、随机种子、内容安全检测
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Wan27TextToVideoRequest extends WanBaseRequest {

    /**
     * 文生视频任务的输入参数
     */
    private TextToVideoInput input;

    @Data
    public static class TextToVideoInput {

        /**
         * 正向提示词
         * 最少1个字符，最多5000个字符
         * 描述想要生成的视频内容
         */
        private String prompt;

        /**
         * 反向提示词
         * 可选，最多500个字符
         * 描述不希望出现在视频中的内容
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 可选的自定义音频URL
         * 可以为生成的视频指定背景音频
         */
        @JsonProperty("audio_url")
        private String audioUrl;

        /**
         * 视频分辨率
         * 允许值: 720p, 1080p
         * 默认值: 1080p
         */
        private String resolution = "1080p";

        /**
         * 视频宽高比
         * 允许值: 16:9(横屏), 9:16(竖屏), 1:1(正方形), 4:3(横向4:3), 3:4(纵向3:4)
         * 默认值: 16:9
         */
        private String ratio = "16:9";

        /**
         * 视频时长，单位秒
         * 最小值: 2，最大值: 15
         * 默认值: 5
         */
        private Integer duration = 5;

        /**
         * 是否开启提示词智能改写
         * 默认值: true
         * 开启后系统会自动优化提示词以获得更好的生成效果
         */
        @JsonProperty("prompt_extend")
        private Boolean promptExtend = true;

        /**
         * 是否添加AI生成水印
         * 默认值: false
         */
        private Boolean watermark = false;

        /**
         * 随机种子
         * 范围: 0 - 2147483647
         * 用于控制生成的随机性，相同种子可复现结果
         */
        private Long seed;

        /**
         * 内容安全检测开关
         * 默认值: false
         * 设置为false时禁用内容过滤功能，所有结果由模型直接返回
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker = false;
    }


}