package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Grok Imagine 图像生成图像请求
 * 模型: grok-imagine/image-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineImageToImageRequest extends GrokImagineBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private ImageToImageInput input;

    @Data
    public static class ImageToImageInput {

        /**
         * 指定生成图像所需内容或样式的文本描述
         * 最大长度：390000 个字符
         * 可通过 @image(n) 引用参考图，例如：@image1 海边日落
         */
        private String prompt;

        /**
         * 参考图像的 URL 列表
         * 最多包含 5 个字符串
         * 支持类型：image/jpeg、image/png、image/webp
         * 单张最大 10.0MB
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;
    }
}