package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Kling 2.6 运动控制请求
 * 模型示例: kling-2.6/motion-control
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Kling26MotionControlRequest extends KlingBaseRequest {
    private MotionControlInput input;

    @Data
    public static class MotionControlInput {
        /**
         * 提示词，最大2500字符
         */
        private String prompt;

        /**
         * 参考图片URL列表
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * 参考视频URL列表
         */
        @JsonProperty("video_urls")
        private List<String> videoUrls;

        /**
         * 角色朝向：image, video
         */
        @JsonProperty("character_orientation")
        private String characterOrientation;

        /**
         * 输出分辨率模式：720p, 1080p
         */
        private String mode;
    }
}