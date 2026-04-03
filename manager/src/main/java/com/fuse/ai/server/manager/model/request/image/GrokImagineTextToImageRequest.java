package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Grok Imagine 文本生成图像请求
 * 模型: grok-imagine/text-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineTextToImageRequest extends GrokImagineBaseRequest {

    /**
     * 图像生成任务的输入参数
     */
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {

        /**
         * 描述期望图像的文本提示
         * 最大长度：5000 字符
         * 支持英文提示
         */
        private String prompt;

        /**
         * 指定生成图像的宽高比
         * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
         * 默认值：1:1
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;
    }
}