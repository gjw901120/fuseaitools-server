package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GPT-4O图像生成回调请求
 */
@Data
public class ImageGpt4oCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 回调数据
     */
    private Gpt4oCallbackData data;

    @Data
    public static class Gpt4oCallbackData {

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 图片生成结果信息
         */
        private Gpt4oInfo info;

        @Data
        public static class Gpt4oInfo {

            /**
             * 生成的图片URL列表
             */
            @JsonProperty("result_urls")
            private String[] resultUrls;
        }
    }
}