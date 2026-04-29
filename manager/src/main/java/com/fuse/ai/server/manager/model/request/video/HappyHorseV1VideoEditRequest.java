package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * HappyHorse V1 视频编辑请求
 * 模型端点固定使用: happyhorse/video-edit
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HappyHorseV1VideoEditRequest extends HappyHorseBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private VideoEditInput input;

    @Data
    public static class VideoEditInput {

        /**
         * 视频编辑指令，用自然语言描述“要怎么改”，必填，最大5000字符
         */
        private String prompt;

        /**
         * 待编辑的视频URL，必须且只能1个
         */
        @JsonProperty("video_url")
        private String videoUrl;

        /**
         * 可选参考图片URL，用于风格/局部替换参考，最多5张
         */
        @JsonProperty("reference_image")
        private List<String> referenceImage;

        /**
         * 输出视频分辨率：720p, 1080p，默认1080p
         */
        private String resolution = "1080p";

        /**
         * 音频处理策略：auto(自动处理)、origin(保留原视频音频)，默认auto
         */
        @JsonProperty("audio_setting")
        private String audioSetting = "auto";

        /**
         * 随机种子，范围0-2147483647
         */
        private Long seed;
    }
}