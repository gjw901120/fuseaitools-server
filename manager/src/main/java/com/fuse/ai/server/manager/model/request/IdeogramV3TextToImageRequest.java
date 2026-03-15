package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ideogram V3 文生图请求
 * 模型示例: ideogram/v3-text-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramV3TextToImageRequest extends IdeogramBaseRequest {
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {
        /**
         * 提示词，最大5000字符
         */
        private String prompt;

        /**
         * 渲染速度：TURBO, BALANCED, QUALITY
         */
        @JsonProperty("rendering_speed")
        private String renderingSpeed;

        /**
         * 风格：AUTO, GENERAL, REALISTIC, DESIGN
         */
        private String style;

        /**
         * 是否使用MagicPrompt扩展提示词
         */
        @JsonProperty("expand_prompt")
        private Boolean expandPrompt;

        /**
         * 图片尺寸：square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
         */
        @JsonProperty("image_size")
        private String imageSize;

        /**
         * 随机数种子
         */
        private Integer seed;

        /**
         * 负面提示词，最大5000字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;
    }
}