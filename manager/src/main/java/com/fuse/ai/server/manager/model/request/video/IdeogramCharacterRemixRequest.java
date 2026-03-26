package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.model.request.video.IdeogramBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Ideogram 角色重混请求
 * 模型示例: ideogram/character-remix
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramCharacterRemixRequest extends IdeogramBaseRequest {
    private CharacterRemixInput input;

    @Data
    public static class CharacterRemixInput {
        /**
         * 提示词，用于重混图片，最大5000字符
         */
        private String prompt;

        /**
         * 输入图片URL
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 角色参考图片URL列表，目前仅支持1张
         */
        @JsonProperty("reference_image_urls")
        private List<String> referenceImageUrls;

        /**
         * 渲染速度：TURBO, BALANCED, QUALITY
         */
        @JsonProperty("rendering_speed")
        private String renderingSpeed;

        /**
         * 风格：AUTO, REALISTIC, FICTION
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
         * 生成图片数量：1, 2, 3, 4
         */
        @JsonProperty("num_images")
        private String numImages;

        /**
         * 随机数种子
         */
        private Integer seed;

        /**
         * 输入图片强度，范围0.1-1，步长0.1，默认0.8
         */
        private BigDecimal strength;

        /**
         * 负面提示词，最大500字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 风格参考图片URL列表
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 参考遮罩URL列表，目前仅支持1张
         */
        @JsonProperty("reference_mask_urls")
        private String referenceMaskUrls;
    }
}