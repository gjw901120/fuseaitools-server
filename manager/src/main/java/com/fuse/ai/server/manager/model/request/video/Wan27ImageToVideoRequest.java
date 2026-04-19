package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通义万相2.7 (Wan2.7) 图生视频请求
 * 模型端点固定使用: wan/2-7-image-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Wan27ImageToVideoRequest extends WanBaseRequest {

    /**
     * 图像转视频任务的输入参数
     */
    private ImageToVideoInput input;

    @Data
    public static class ImageToVideoInput {

        /**
         * 正向提示词，可选，最多5000个字符
         */
        private String prompt;

        /**
         * 反向提示词，可选，最多500个字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 首帧图片URL，可选
         */
        @JsonProperty("first_frame_url")
        private String firstFrameUrl;

        /**
         * 尾帧图片URL，可选
         */
        @JsonProperty("last_frame_url")
        private String lastFrameUrl;

        /**
         * 首段视频URL，用于视频续写，可选
         */
        @JsonProperty("first_clip_url")
        private String firstClipUrl;

        /**
         * 驱动音频URL，可选
         */
        @JsonProperty("driving_audio_url")
        private String drivingAudioUrl;

        /**
         * 视频分辨率：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 视频时长，单位秒，范围2-15，默认5
         */
        private Integer duration = 5;

        /**
         * 是否开启提示词智能改写，默认true
         */
        @JsonProperty("prompt_extend")
        private Boolean promptExtend = true;

        /**
         * 是否添加AI生成水印，默认false
         */
        private Boolean watermark = false;

        /**
         * 随机种子，范围0-2147483647
         */
        private Long seed;

        /**
         * 内容安全检测开关，默认false
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker = false;
    }
}