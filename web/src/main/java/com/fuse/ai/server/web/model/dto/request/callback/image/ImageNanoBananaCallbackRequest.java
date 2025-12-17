package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Nano Banana图像生成回调请求
 */
@Data
public class ImageNanoBananaCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private NanoBananaCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class NanoBananaCallbackData {

        /**
         * 任务完成时间
         */
        private Long completeTime;

        /**
         * 消耗积分
         */
        private Integer consumeCredits;

        /**
         * 耗时（秒）
         */
        private Integer costTime;

        /**
         * 任务创建时间
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
         * 剩余积分
         */
        private Integer remainedCredits;

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
         * 更新时间
         */
        private Long updateTime;
    }

    /**
     * Nano Banana参数
     */
    @Data
    public static class NanoBananaParam {

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
        private NanoBananaInput input;

        @Data
        public static class NanoBananaInput {

            /**
             * 提示词
             */
            private String prompt;

            /**
             * 输出格式
             */
            @JsonProperty("output_format")
            private String outputFormat;

            /**
             * 图片尺寸
             */
            @JsonProperty("image_size")
            private String imageSize;
        }
    }

    /**
     * Nano Banana结果
     */
    @Data
    public static class NanoBananaResult {

        /**
         * 结果URL数组
         */
        private String[] resultUrls;
    }
}