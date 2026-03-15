package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Ideogram图像生成回调请求
 */
@Data
public class ImageIdeogramCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private IdeogramCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class IdeogramCallbackData {

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
     * Ideogram参数解析
     */
    @Data
    public static class IdeogramParam {

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
        private IdeogramInput input;

        @Data
        public static class IdeogramInput {

            /**
             * 提示词
             */
            private String prompt;

            /**
             * 图片URL
             */
            @JsonProperty("image_url")
            private String imageUrl;

            /**
             * 遮罩URL
             */
            @JsonProperty("mask_url")
            private String maskUrl;

            /**
             * 参考图片URL列表
             */
            @JsonProperty("reference_image_urls")
            private List<String> referenceImageUrls;

            /**
             * 渲染速度：TURBO, BALANCED, QUALITY
             */
            @JsonProperty("rendering_speed")
            private String renderingSpeed;

            /**
             * 风格：AUTO, GENERAL, REALISTIC, DESIGN, FICTION
             */
            private String style;

            /**
             * 是否使用MagicPrompt扩展提示词
             */
            @JsonProperty("expand_prompt")
            private Boolean expandPrompt;

            /**
             * 图片尺寸
             */
            @JsonProperty("image_size")
            private String imageSize;

            /**
             * 生成图片数量：1, 2, 3, 4
             */
            @JsonProperty("num_images")
            private String numImages;

            /**
             * 随机数种子
             */
            private Integer seed;

            /**
             * 输入图片强度
             */
            private Double strength;

            /**
             * 负面提示词
             */
            @JsonProperty("negative_prompt")
            private String negativePrompt;

            /**
             * 风格参考图片URL列表
             */
            @JsonProperty("image_urls")
            private List<String> imageUrls;

            /**
             * 参考遮罩URL
             */
            @JsonProperty("reference_mask_urls")
            private String referenceMaskUrls;
        }
    }

    /**
     * Ideogram结果解析
     */
    @Data
    public static class IdeogramResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}