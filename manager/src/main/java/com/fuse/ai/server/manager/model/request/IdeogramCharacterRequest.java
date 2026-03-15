package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Ideogram 角色生成请求
 * 模型示例: ideogram/character
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramCharacterRequest extends IdeogramBaseRequest {
    private CharacterInput input;

    @Data
    public static class CharacterInput {
        /**
         * 提示词，用于填充遮罩部分，最大5000字符
         */
        private String prompt;

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
         * 生成图片数量：1, 2, 3, 4
         */
        @JsonProperty("num_images")
        private String numImages;

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