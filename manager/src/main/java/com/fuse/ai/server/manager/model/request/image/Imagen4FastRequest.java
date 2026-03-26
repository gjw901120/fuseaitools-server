package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Imagen 4 Fast 图像生成请求
 * 模型: google/imagen4-fast
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Imagen4FastRequest extends ImagenBaseRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成任务的输入参数
     */
    private Imagen4FastInput input;

    @Data
    public static class Imagen4FastInput {

        /**
         * 用于描述生成图像内容的文本提示词
         * 必填字段，最大长度：5000 字符
         */
        private String prompt;

        /**
         * 用于描述生成图像中需要规避的元素的文本
         * 可选字段，最大长度：5000 字符
         */
        @JsonProperty("negative_prompt")
        private String negativePrompt;

        /**
         * 生成图像的宽高比
         * 可选字段
         * 可选值：1:1, 16:9, 9:16, 3:4, 4:3
         * 默认值：16:9
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 生成图像的数量
         * 可选字段
         * 可选值：1, 2, 3, 4
         * 默认值：1
         */
        @JsonProperty("num_images")
        private String numImages;

        /**
         * 用于生成结果可复现的随机种子值
         * 可选字段
         */
        private Integer seed;
    }
}