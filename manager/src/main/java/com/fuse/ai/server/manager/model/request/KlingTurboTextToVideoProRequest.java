package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Kling Turbo 文生视频 Pro版请求
 * 模型示例: kling/v2-5-turbo-text-to-video-pro
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KlingTurboTextToVideoProRequest extends KlingBaseRequest {
    private TextToVideoProInput input;

    @Data
    public static class TextToVideoProInput {
        /**
         * 提示词，最大2500字符
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
         * 负面提示词，避免的内容，最大2500字符
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