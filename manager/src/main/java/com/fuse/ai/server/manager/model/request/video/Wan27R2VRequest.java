package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 通义万相2.7 (Wan2.7) 参考驱动视频生成请求
 * 模型端点固定使用: wan/2-7-r2v
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Wan27R2VRequest extends WanBaseRequest {

    /**
     * 参考驱动视频生成任务的输入参数
     */
    private R2VInput input;

    @Data
    public static class R2VInput {

        /**
         * 文本提示词，必填
         * 用来描述生成视频中期望包含的元素和视觉特点
         * 支持中英文，最大长度5000字符
         */
        private String prompt;

        /**
         * 反向提示词，可选
         * 用来描述不希望在视频画面中出现的内容
         * 支持中英文，最大长度500字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 参考图像URL数组，可选
         * 参考图像与参考视频至少传入一种
         * 图像数 + 视频数总和不能超过5
         */
        @JsonProperty("reference_image")
        private List<String> referenceImage;

        /**
         * 参考视频URL数组，可选
         * 参考图像与参考视频至少传入一种
         * 图像数 + 视频数总和不能超过5
         */
        @JsonProperty("reference_video")
        private List<String> referenceVideo;

        /**
         * 首帧图像URL，可选，最多传入1张
         * 传入后会自动忽略aspect_ratio，以首帧图像宽高比生成近似比例视频
         */
        @JsonProperty("first_frame")
        private String firstFrame;

        /**
         * 音频URL，可选
         * 用于指定参考素材主体角色的音色
         * 格式：wav、mp3，时长：1到10秒，文件大小：不超过15MB
         */
        @JsonProperty("reference_voice")
        private String referenceVoice;

        /**
         * 输出视频分辨率档位：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 输出视频宽高比，可选
         * 允许值：16:9、9:16、1:1、4:3、3:4
         * 默认16:9，传入first_frame时自动忽略
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio = "16:9";

        /**
         * 输出视频时长，单位秒，范围2-10，默认5
         */
        private Integer duration = 5;

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