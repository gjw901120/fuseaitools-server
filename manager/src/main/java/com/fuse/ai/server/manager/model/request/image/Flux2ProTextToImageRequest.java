package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Flux 2 Pro 文本生成图像请求
 * 模型: flux-2/pro-text-to-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Flux2ProTextToImageRequest extends FluxBaseRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成任务的输入参数
     */
    private Flux2ProTextToImageInput input;

    @Data
    public static class Flux2ProTextToImageInput {

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