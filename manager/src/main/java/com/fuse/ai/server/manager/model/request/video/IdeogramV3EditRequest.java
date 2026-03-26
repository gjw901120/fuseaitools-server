package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.model.request.video.IdeogramBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ideogram V3 编辑请求
 * 模型示例: ideogram/v3-edit
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeogramV3EditRequest extends IdeogramBaseRequest {
    private EditInput input;

    @Data
    public static class EditInput {
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
         * 渲染速度：TURBO, BALANCED, QUALITY
         */
        @JsonProperty("rendering_speed")
        private String renderingSpeed;

        /**
         * 是否使用MagicPrompt扩展提示词
         */
        @JsonProperty("expand_prompt")
        private Boolean expandPrompt;

        /**
         * 随机数种子
         */
        private Integer seed;
    }
}