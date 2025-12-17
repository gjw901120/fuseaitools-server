package com.fuse.ai.server.web.model.dto.request.callback.image;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Flux Kontext图像生成回调请求
 */
@Data
public class ImageFluxKontextCallbackRequest {

    /**
     * 状态码
     */
    @NotNull(message = "Code cannot be empty")
    private Integer code;

    /**
     * 状态消息
     */
    private String msg;

    /**
     * 回调数据
     */
    private FluxKontextCallbackData data;

    @Data
    public static class FluxKontextCallbackData {

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 图片生成结果信息
         */
        private FluxKontextInfo info;

        @Data
        public static class FluxKontextInfo {

            /**
             * 原始图像URL
             */
            private String originImageUrl;

            /**
             * 生成图像URL
             */
            private String resultImageUrl;
        }
    }
}