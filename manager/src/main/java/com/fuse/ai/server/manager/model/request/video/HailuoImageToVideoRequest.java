package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.model.request.video.HailuoBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Hailuo 图生视频请求
 * 可用于标准版(standard)和Pro版(pro)，通过model字段区分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HailuoImageToVideoRequest extends HailuoBaseRequest {
    private ImageToVideoInput input;

    @Data
    public static class ImageToVideoInput {
        /**
         * 提示词，1-5000字符
         */
        private String prompt;

        /**
         * 图片URL，单张图片
         * 支持格式：JPEG/PNG/WEBP，最大10MB
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 视频时长：6, 10（10秒不支持1080P分辨率）
         */
        private String duration;

        /**
         * 分辨率：768P, 1080P
         */
        private String resolution;
    }
}