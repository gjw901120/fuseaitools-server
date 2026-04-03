package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Qwen 2 图像编辑请求
 * 模型: qwen2/image-edit
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Qwen2ImageEditRequest extends QwenBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private ImageEditInput input;

    @Data
    public static class ImageEditInput {

        /**
         * 用于图像生成的文本提示词
         * 最大长度：800 字符
         */
        private String prompt;

        /**
         * 待编辑图像的 URL
         * 支持的类型：image/jpeg、image/png、image/webp
         * 最大文件大小：10.0MB
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 生成图像的尺寸规格
         * 可选值：1:1, 2:3, 3:2, 3:4, 4:3, 9:16, 16:9, 21:9
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