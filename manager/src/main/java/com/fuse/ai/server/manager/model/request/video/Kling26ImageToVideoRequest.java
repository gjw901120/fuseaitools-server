package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Kling 2.6 图生视频请求
 * 模型示例: kling-2.6/image-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Kling26ImageToVideoRequest extends KlingBaseRequest {
    private ImageToVideo26Input input;

    @Data
    public static class ImageToVideo26Input {
        /**
         * 提示词，最大2500字符
         */
        private String prompt;

        /**
         * 图片URL列表，用于视频生成
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 是否包含声音
         */
        private Boolean sound;

        /**
         * 视频时长：5, 10
         */
        private String duration;
    }
}