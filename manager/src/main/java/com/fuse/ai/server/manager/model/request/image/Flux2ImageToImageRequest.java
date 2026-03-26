package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * Flux 2 图像生成图像请求
 * 模型: flux-2/flex-image-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Flux2ImageToImageRequest extends FluxBaseRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成任务的输入参数
     */
    private Flux2ImageToImageInput input;

    @Data
    public static class Flux2ImageToImageInput {

        /**
         * 输入参考图像 URL 列表
         * 必填字段，1-8 张图片
         * 支持的格式：image/jpeg、image/png、image/webp
         * 单张图片最大大小：10.0MB
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * 文本提示词
         * 必填字段，长度必须在 3-5000 字符之间
         */
        private String prompt;

        /**
         * 生成图像的宽高比
         * 必填字段
         * 可选值：1:1, 4:3, 3:4, 16:9, 9:16, 3:2, 2:3, auto
         * 默认值：1:1
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 输出图像分辨率
         * 必填字段
         * 可选值：1K, 2K
         * 默认值：1K
         */
        private String resolution;

        /**
         * NSFW 内容检测器
         * 可选字段
         * Playground 中默认启用，API 调用可根据需要配置
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker;
    }
}