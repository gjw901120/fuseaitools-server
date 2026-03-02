package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Qwen图像生成回调请求
 */
@Data
public class ImageQwenCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private QwenCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class QwenCallbackData {

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
     * Qwen参数解析
     */
    @Data
    public static class QwenParam {

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
        private QwenInput input;

        @Data
        public static class QwenInput {

            /**
             * 提示词
             */
            private String prompt;

            /**
             * 参考图片URL
             */
            @JsonProperty("image_url")
            private String imageUrl;

            /**
             * 去噪强度
             */
            private Double strength;

            /**
             * 输出格式
             */
            @JsonProperty("output_format")
            private String outputFormat;

            /**
             * 加速级别
             */
            private String acceleration;

            /**
             * 负向提示词
             */
            @JsonProperty("negative_prompt")
            private String negativePrompt;

            /**
             * 随机种子
             */
            private Integer seed;

            /**
             * 推理步数
             */
            @JsonProperty("num_inference_steps")
            private Integer numInferenceSteps;

            /**
             * 引导比例
             */
            @JsonProperty("guidance_scale")
            private Double guidanceScale;

            /**
             * 是否启用安全过滤器
             */
            @JsonProperty("enable_safety_checker")
            private Boolean enableSafetyChecker;
        }
    }

    /**
     * Qwen结果解析
     */
    @Data
    public static class QwenResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}