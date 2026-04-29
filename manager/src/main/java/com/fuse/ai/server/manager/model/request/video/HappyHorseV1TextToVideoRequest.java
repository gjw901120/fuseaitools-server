package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * HappyHorse V1 文生视频请求
 * 模型端点固定使用: happyhorse/text-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HappyHorseV1TextToVideoRequest extends HappyHorseBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private TextToVideoInput input;

    @Data
    public static class TextToVideoInput {

        /**
         * 用于描述要生成的视频内容、场景与风格的文本提示词，必填，最大5000字符
         */
        private String prompt;

        /**
         * 输出视频分辨率：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 输出视频的宽高比：16:9、9:16、1:1、4:3、3:4，默认16:9
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio = "16:9";

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