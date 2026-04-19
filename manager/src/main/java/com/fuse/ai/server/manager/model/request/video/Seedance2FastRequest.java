package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Seedance 2 Fast 文生视频请求
 * 模型端点固定使用: bytedance/seedance-2-fast
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Seedance2FastRequest extends SeedanceBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private Seedance2FastInput input;

    @Data
    public static class Seedance2FastInput {

        /**
         * 用于视频生成的文本提示词
         * 最小长度：3，最大长度：20000字符，必填
         */
        private String prompt;

        /**
         * 首帧图片URL或者asset://{assetId}
         * 例如: asset://asset-20260404242101-76djj
         */
        @JsonProperty("first_frame_url")
        private String firstFrameUrl;

        /**
         * 尾帧图片URL或者asset://{assetId}
         * 例如: asset://asset-20260404242101-76djj
         */
        @JsonProperty("last_frame_url")
        private String lastFrameUrl;

        /**
         * 输入图像URL或者asset://{assetId}列表
         * 最大文件数：和首尾帧张数之和不得超过9张
         */
        @JsonProperty("reference_image_urls")
        private List<String> referenceImageUrls;

        /**
         * 输入视频URL或者asset://{assetId}列表
         * 最多传入3个参考视频，所有视频总时长不超过15s
         */
        @JsonProperty("reference_video_urls")
        private List<String> referenceVideoUrls;

        /**
         * 输入音频URL或者asset://{assetId}列表
         * 最多传入3段参考音频，所有音频总时长不超过15s
         */
        @JsonProperty("reference_audio_urls")
        private List<String> referenceAudioUrls;

        /**
         * 是否返回视频最后一帧图片，已弃用
         */
        @JsonProperty("return_last_frame")
        @Deprecated
        private Boolean returnLastFrame = false;

        /**
         * 是否生成与画面同步的音频，仅部分模型支持，默认true
         */
        @JsonProperty("generate_audio")
        private Boolean generateAudio = true;

        /**
         * 视频分辨率：480p, 720p，默认720p
         */
        private String resolution = "720p";

        /**
         * 视频画面比例配置
         * 允许值：1:1、4:3、3:4、16:9、9:16、21:9、adaptive
         * 默认16:9
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio = "16:9";

        /**
         * 视频时长4-15（秒），默认5
         */
        private Integer duration = 5;

        /**
         * 是否启用联网搜索，必填
         */
        @JsonProperty("web_search")
        private Boolean webSearch;

        /**
         * 内容安全检测开关，默认false
         * 设置为false时禁用内容过滤功能，所有结果由模型直接返回
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker = false;
    }
}