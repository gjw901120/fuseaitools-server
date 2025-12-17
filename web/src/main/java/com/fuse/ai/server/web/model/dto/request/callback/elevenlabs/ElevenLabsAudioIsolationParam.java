package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ElevenLabs音频分离参数
 */
@Data
public class ElevenLabsAudioIsolationParam {

    /**
     * 回调URL
     */
    private String callBackUrl;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 输入参数
     */
    private AudioIsolationInput input;

    @Data
    public static class AudioIsolationInput {
        @JsonProperty("audio_url")
        private String audioUrl;
    }

    /**
     * 音频分离结果
     */
    @Data
    public static class AudioIsolationResult {
        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private String[] resultUrls;
    }
}