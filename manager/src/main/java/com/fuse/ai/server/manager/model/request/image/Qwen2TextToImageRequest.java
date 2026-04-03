package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Qwen 2 图像编辑请求
 * 模型: qwen2/text-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Qwen2TextToImageRequest extends QwenBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {

        /**
         * 用于图像生成的文本提示词
         * 最大长度：800 字符
         */
        private String prompt;

        /**
         * 生成图像的尺寸规格
         * 可选值：1:1, 3:4, 4:3, 9:16, 16:9
         * 默认值：16:9
         */
        @JsonProperty("image_size")
        private String imageSize;

        /**
         * 随机种子值
         * 相同的种子值、提示词和模型版本，每次生成的图像结果完全一致
         */
        private Integer seed;

        /**
         * 生成图像的输出格式
         * 可选值：jpeg, png
         * 默认值：png
         */
        @JsonProperty("output_format")
        private String outputFormat;
    }
}