package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Kling AI头像 标准版请求
 * 模型示例: kling/ai-avatar-standard
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KlingAIAvatarStandardRequest extends KlingBaseRequest {
    private AIAvatarInput input;

    @Data
    public static class AIAvatarInput {
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