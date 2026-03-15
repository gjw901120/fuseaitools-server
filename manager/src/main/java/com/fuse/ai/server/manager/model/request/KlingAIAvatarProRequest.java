package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Kling AI头像 Pro版请求
 * 模型示例: kling/ai-avatar-pro
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KlingAIAvatarProRequest extends KlingBaseRequest {
    private AIAvatarProInput input;

    @Data
    public static class AIAvatarProInput {
        /**
         * 头像图片URL
         */
        @JsonProperty("image_url")
        private String imageUrl;

        /**
         * 音频文件URL
         */
        @JsonProperty("audio_url")
        private String audioUrl;

        /**
         * 提示词，最大5000字符
         */
        private String prompt;
    }
}