package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Kling视频生成回调请求
 */
@Data
public class KlingCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private KlingCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class KlingCallbackData {

        /**
         * 任务完成时间戳（毫秒）
         */
        private Long completeTime;

        /**
         * 任务耗时（秒）
         */
        private Integer costTime;

        /**
         * 任务创建时间戳（毫秒）
         */
        private Long createTime;

        /**
         * 模型名称
         */
        private String model;

        /**
         * 任务参数（JSON字符串）
         */
        private String param;

        /**
         * 结果JSON
         */
        private String resultJson;

        /**
         * 任务状态
         */
        private String state;

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 失败码
         */
        private String failCode;

        /**
         * 失败信息
         */
        private String failMsg;
    }

    /**
     * Kling参数解析
     */
    @Data
    public static class KlingParam {

        /**
         * 回调URL
         */
        private String callBackUrl;

        /**
         * 模型名称
         */
        private String model;

        /**
         * 输入参数
         */
        private KlingInput input;

        @Data
        public static class KlingInput {

            /**
             * 图片URL
             */
            @JsonProperty("image_url")
            private String imageUrl;

            /**
             * 音频URL
             */
            @JsonProperty("audio_url")
            private String audioUrl;

            /**
             * 提示词
             */
            private String prompt;

            /**
             * 视频时长：5, 10
             */
            private String duration;

            /**
             * 视频宽高比：16:9, 9:16, 1:1
             */
            @JsonProperty("aspect_ratio")
            private String aspectRatio;

            /**
             * 负面提示词
             */
            @JsonProperty("negative_prompt")
            private String negativePrompt;

            /**
             * CFG引导尺度，范围0-1
             */
            @JsonProperty("cfg_scale")
            private Double cfgScale;

            /**
             * 尾帧图片URL
             */
            @JsonProperty("tail_image_url")
            private String tailImageUrl;

            /**
             * 图片URL列表
             */
            @JsonProperty("image_urls")
            private List<String> imageUrls;

            /**
             * 是否包含声音
             */
            private Boolean sound;

            /**
             * 参考图片URL列表
             */
            @JsonProperty("input_urls")
            private List<String> inputUrls;

            /**
             * 参考视频URL列表
             */
            @JsonProperty("video_urls")
            private List<String> videoUrls;

            /**
             * 角色朝向：image, video
             */
            @JsonProperty("character_orientation")
            private String characterOrientation;

            /**
             * 输出分辨率模式：720p, 1080p, std, pro
             */
            private String mode;

            /**
             * 生成模式：std, pro
             */
            @JsonProperty("mode")
            private String generationMode;

            /**
             * 多镜头提示词数组
             */
            @JsonProperty("multi_prompt")
            private List<MultiPrompt> multiPrompt;

            /**
             * 是否启用多镜头模式
             */
            @JsonProperty("multi_shots")
            private Boolean multiShots;

            /**
             * Kling元素数组
             */
            @JsonProperty("kling_elements")
            private List<KlingElement> klingElements;
        }

        @Data
        public static class MultiPrompt {
            /**
             * 提示词
             */
            private String prompt;

            /**
             * 时长，1-12秒
             */
            private Integer duration;
        }

        @Data
        public static class KlingElement {
            /**
             * 元素名称，用于@element_name引用
             */
            private String name;

            /**
             * 元素描述
             */
            private String description;

            /**
             * 元素图片URL列表，2-50张
             */
            @JsonProperty("element_input_urls")
            private List<String> elementInputUrls;
        }
    }

    /**
     * Kling结果解析
     */
    @Data
    public static class KlingResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}