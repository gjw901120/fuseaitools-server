package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GPT Image 2 文生图请求
 * 模型端点固定使用: gpt-image-2-text-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GptImageV2TextToImageRequest extends GptImageBaseRequest {

    /**
     * 文生图任务的输入参数
     */
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {

        /**
         * 文本提示词，必填，最多20000个字符
         */
        private String prompt;

        /**
         * 生成图片的比例，默认auto
         * 允许值：auto、1:1、9:16、16:9、4:3、3:4
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio = "auto";

        /**
         * 图片的分辨率
         * 允许值：1K、2K、4K
         * 注意：1:1比例的图片无法生成4K图片
         */
        private String resolution;
    }
}