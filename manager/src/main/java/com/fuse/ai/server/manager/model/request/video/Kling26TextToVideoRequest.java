package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Kling 2.6 文生视频请求
 * 模型示例: kling-2.6/text-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Kling26TextToVideoRequest extends KlingBaseRequest {
    private TextToVideo26Input input;

    @Data
    public static class TextToVideo26Input {
        /**
         * 提示词，最大2500字符
         */
        private String prompt;

        /**
         * 是否包含声音
         */
        private Boolean sound;

        /**
         * 视频宽高比：1:1, 16:9, 9:16
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 视频时长：5, 10
         */
        private String duration;
    }
}