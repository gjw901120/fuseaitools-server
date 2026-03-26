package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Kling 3.0 视频生成请求
 * 模型示例: kling-3.0/video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Kling30VideoRequest extends KlingBaseRequest {
    private Video30Input input;

    @Data
    public static class Video30Input {
        /**
         * 生成模式：std, pro
         */
        private String mode;

        /**
         * 图片URL列表
         * 单镜头：[start_frame_url, end_frame_url]
         * 多镜头：[start_frame_url]
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 提示词（单镜头模式必填）
         */
        private String prompt;

        /**
         * 多镜头提示词数组（多镜头模式必填）
         */
        @JsonProperty("multi_prompt")
        private List<MultiPrompt> multiPrompt;

        /**
         * 视频时长，单镜头：3-15秒，多镜头：各镜头总时长3-15秒
         */
        private String duration;

        /**
         * 是否启用多镜头模式
         */
        @JsonProperty("multi_shots")
        private Boolean multiShots;

        /**
         * 是否启用声音效果
         */
        private Boolean sound;

        /**
         * Kling元素数组，可在提示词中引用
         */
        @JsonProperty("kling_elements")
        private List<KlingElement> klingElements;

        /**
         * 视频宽高比：16:9, 9:16, 1:1
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;
    }

    @Data
    public static class MultiPrompt {
        /**
         * 提示词，最大500字符
         */
        private String prompt;

        /**
         * 时长，1-12秒
         */
        private Integer duration;
    }

    @Data
    public static class KlingElement {
        /**
         * 元素名称，用于@element_name引用
         */
        private String name;

        /**
         * 元素描述
         */
        private String description;

        /**
         * 元素图片URL列表，2-50张
         */
        @JsonProperty("element_input_urls")
        private List<String> elementInputUrls;
    }
}