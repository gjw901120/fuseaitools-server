package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * GPT Image图像生成回调请求
 */
@Data
public class ImageGptImageCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private GptImageCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class GptImageCallbackData {

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
     * GPT Image参数解析
     */
    @Data
    public static class GptImageParam {

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
        private GptImageInput input;

        @Data
        public static class GptImageInput {

            /**
             * 输入图片URL列表
             */
            @JsonProperty("input_urls")
            private List<String> inputUrls;

            /**
             * 提示词
             */
            private String prompt;

            /**
             * 图像宽高比：1:1, 2:3, 3:2
             */
            @JsonProperty("aspect_ratio")
            private String aspectRatio;

            /**
             * 画质：medium（平衡）或 high（慢/细节）
             */
            private String quality;
        }
    }

    /**
     * GPT Image结果解析
     */
    @Data
    public static class GptImageResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}