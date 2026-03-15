package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Hailuo视频生成回调请求
 */
@Data
public class HailuoCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private HailuoCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class HailuoCallbackData {

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
     * Hailuo参数解析
     */
    @Data
    public static class HailuoParam {

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
        private HailuoInput input;

        @Data
        public static class HailuoInput {

            /**
             * 提示词，最大5000字符
             */
            private String prompt;

            /**
             * 输入图片URL，格式为JPEG/PNG/WEBP，最大10MB
             */
            @JsonProperty("image_url")
            private String imageUrl;

            /**
             * 视频时长：6秒或10秒（10秒不支持1080P分辨率）
             */
            private String duration;

            /**
             * 视频分辨率：768P 或 1080P
             */
            private String resolution;
        }
    }

    /**
     * Hailuo结果解析
     */
    @Data
    public static class HailuoResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}