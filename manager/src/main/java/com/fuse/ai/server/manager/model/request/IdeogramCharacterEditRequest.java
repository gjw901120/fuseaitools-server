package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Ideogram 角色编辑请求
 * 模型示例: ideogram/character-edit
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramCharacterEditRequest extends IdeogramBaseRequest {
    private CharacterEditInput input;

    @Data
    public static class CharacterEditInput {
        /**
         * 提示词，用于填充遮罩部分，最大5000字符
         */
        private String prompt;

        /**
         * 输入图片URL
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 遮罩图片URL，需与输入图片尺寸匹配
         */
        @JsonProperty("mask_url")
        private String maskUrl;

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
         * 随机数种子
         */
        private Integer seed;
    }
}