package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通义万相2.7 (Wan2.7) 视频编辑请求
 * 模型端点固定使用: wan/2-7-videoedit
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Wan27VideoEditRequest extends WanBaseRequest {

    /**
     * 视频编辑任务的输入参数
     */
    private VideoEditInput input;

    @Data
    public static class VideoEditInput {

        /**
         * 文本提示词，用来描述生成视频中期望包含的元素和视觉特点
         * 支持中英文，最大长度5000字符，可选
         */
        private String prompt;

        /**
         * 反向提示词，用来描述不希望出现在视频画面中的内容
         * 支持中英文，最大长度500字符，可选
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 待编辑视频的URL，必填
         * 格式：mp4、mov，时长：2到10秒
         * 分辨率：宽高范围[240,4096]像素，宽高比：1:8到8:1
         * 文件大小：不超过100MB
         */
        @JsonProperty("video_url")
        private String videoUrl;

        /**
         * 参考图像URL，用于人物、服饰、风格等参考，可选
         * 格式：JPEG、JPG、PNG、BMP、WEBP
         * 分辨率：宽高范围[240,8000]像素，宽高比：1:8到8:1
         * 文件大小：不超过20MB
         */
        @JsonProperty("reference_image")
        private String referenceImage;

        /**
         * 输出视频分辨率档位：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 输出视频宽高比，可选
         * 允许值：16:9、9:16、1:1、4:3、3:4
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 输出视频时长，单位秒
         * 默认0表示使用输入视频时长，不截断
         * 合法取值为0或[2,10]之间的整数
         */
        private Integer duration = 0;

        /**
         * 视频声音设置：auto(智能判断是否重生成音频)，origin(强制保留原声)
         * 默认auto
         */
        @JsonProperty("audio_setting")
        private String audioSetting = "auto";

        /**
         * 是否开启prompt智能改写，默认true
         */
        @JsonProperty("prompt_extend")
        private Boolean promptExtend = true;

        /**
         * 是否添加水印，水印位于视频右下角，文案固定为"AI生成"，默认false
         */
        private Boolean watermark = false;

        /**
         * 随机种子，范围0-2147483647，未传时系统自动生成
         */
        private Long seed;

        /**
         * 内容安全检测开关，默认false
         * 设置为false时禁用内容过滤功能，所有结果由模型直接返回
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker = false;
    }
}