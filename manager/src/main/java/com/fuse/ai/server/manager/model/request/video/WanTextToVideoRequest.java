package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文生视频请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WanTextToVideoRequest extends WanBaseRequest {
    private TextToVideoInput input;

    @Data
    public static class TextToVideoInput {
        /**
         * 提示词，1-5000字符
         */
        private String prompt;

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