package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.model.request.video.KlingBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Kling Turbo 图生视频 Pro版请求
 * 模型示例: kling/v2-5-turbo-image-to-video-pro
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KlingTurboImageToVideoProRequest extends KlingBaseRequest {
    private ImageToVideoProInput input;

    @Data
    public static class ImageToVideoProInput {
        /**
         * 提示词，最大2500字符
         */
        private String prompt;

        /**
         * 图片URL，用于视频生成
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 尾帧图片URL
         */
        @JsonProperty("tail_image_url")
        private String tailImageUrl;

        /**
         * 视频时长：5, 10
         */
        private String duration;

        /**
         * 负面提示词，避免的内容，最大2496字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * CFG引导尺度，范围0-1，步长0.1
         */
        @JsonProperty("cfg_scale")
        private BigDecimal cfgScale;
    }
}