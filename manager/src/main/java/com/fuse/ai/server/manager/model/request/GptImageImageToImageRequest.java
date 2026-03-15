package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * GPT Image 1.5 图生图请求
 * 模型示例: gpt-image/1.5-image-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GptImageImageToImageRequest extends GptImageBaseRequest {
    private ImageToImageInput input;

    @Data
    public static class ImageToImageInput {
        /**
         * 输入图片URL列表
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * 提示词，最大3000字符
         */
        private String prompt;

        /**
         * 宽高比：1:1, 2:3, 3:2
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 质量：medium（平衡）, high（慢/细节）
         */
        private String quality;
    }
}