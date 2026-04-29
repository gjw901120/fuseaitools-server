package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * HappyHorse V1 图生视频请求
 * 模型端点固定使用: happyhorse/image-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HappyHorseV1ImageToVideoRequest extends HappyHorseBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private ImageToVideoInput input;

    @Data
    public static class ImageToVideoInput {

        /**
         * 可选的文本提示词，用于补充/约束由首帧图驱动的视频内容，最大5000字符
         */
        private String prompt;

        /**
         * 输入首帧图片URL，必须且只能1张
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 输出视频分辨率：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 输出视频时长（秒），范围3-15，默认5
         */
        private Integer duration = 5;

        /**
         * 随机种子，范围0-2147483647
         */
        private Long seed;
    }
}