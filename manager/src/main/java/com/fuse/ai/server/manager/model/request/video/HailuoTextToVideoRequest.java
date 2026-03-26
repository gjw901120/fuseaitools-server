package com.fuse.ai.server.manager.model.request.video;

import com.fuse.ai.server.manager.model.request.video.HailuoBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Hailuo 文生视频请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HailuoTextToVideoRequest extends HailuoBaseRequest {
    private TextToVideoInput input;

    @Data
    public static class TextToVideoInput {
        /**
         * 提示词，1-5000字符
         */
        private String prompt;

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
