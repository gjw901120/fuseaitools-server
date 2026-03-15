package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ideogram V3 重构请求
 * 模型示例: ideogram/v3-reframe
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramV3ReframeRequest extends IdeogramBaseRequest {
    private ReframeInput input;

    @Data
    public static class ReframeInput {
        /**
         * 输入图片URL
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 输出图片尺寸：square, square_hd, portrait_4_3, portrait_16_9, landscape_4_3, landscape_16_9
         */
        @JsonProperty("image_size")
        private String imageSize;

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
         * 生成图片数量：1, 2, 3, 4
         */
        @JsonProperty("num_images")
        private String numImages;

        /**
         * 随机数种子
         */
        private Integer seed;
    }
}