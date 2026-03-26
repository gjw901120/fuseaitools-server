package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 图生视频请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WanImageToVideoRequest extends WanBaseRequest {
    private ImageToVideoInput input;

    @Data
    public static class ImageToVideoInput {
        /**
         * 提示词，1-5000字符
         */
        private String prompt;

        /**
         * 图片URL列表，至少256x256px，支持JPEG/PNG/WEBP，最大10MB
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 视频时长：5, 10, 15
         */
        private String duration;

        /**
         * 分辨率：720p, 1080p
         */
        private String resolution;

        /**
         * 是否多镜头
         */
        @JsonProperty("multi_shots")
        private Boolean multiShots;
    }
}