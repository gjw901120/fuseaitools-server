package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 视频生视频请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WanVideoToVideoRequest extends WanBaseRequest {
    private VideoToVideoInput input;

    @Data
    public static class VideoToVideoInput {
        /**
         * 提示词，1-5000字符
         */
        private String prompt;

        /**
         * 视频URL列表，支持MP4/MOV/MKV，最大10MB
         */
        @JsonProperty("video_urls")
        private List<String> videoUrls;

        /**
         * 视频时长：5, 10（文档中仅5和10可选）
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