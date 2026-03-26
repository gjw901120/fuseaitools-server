package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * Nano Banana 2 图像生成请求
 * 模型: nano-banana-2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NanoBanana2Request extends NanoBananaBaseRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成任务的输入参数
     */
    private NanoBanana2Input input;

    @Data
    public static class NanoBanana2Input {

        /**
         * 用于描述待生成图像的文本提示词
         * 必填字段，最大长度：20000 字符
         */
        private String prompt;

        /**
         * 用于图像变换或作为参考的输入图像 URL
         * 可选字段，最多支持 14 张图片
         * 支持的格式：image/jpeg、image/png、image/webp
         * 单张图片最大大小：30.0MB
         */
        @JsonProperty("image_input")
        private List<String> imageInput;

        /**
         * 生成图像的宽高比
         * 可选字段
         * 可选值：auto, 1:1, 1:4, 16:9, 1:8, 21:9, 2:3, 3:2, 3:4, 4:1, 4:3, 4:5, 5:4, 8:1, 9:16
         * 默认值：auto
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 生成图像的分辨率
         * 可选字段
         * 可选值：1K, 2K, 4K
         * 默认值：1K
         */
        private String resolution;

        /**
         * 生成图像的输出格式
         * 可选字段
         * 可选值：jpg, png
         * 默认值：jpg
         */
        @JsonProperty("output_format")
        private String outputFormat;
    }
}