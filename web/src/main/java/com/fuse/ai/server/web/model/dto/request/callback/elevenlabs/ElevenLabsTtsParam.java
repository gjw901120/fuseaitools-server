package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ElevenLabs文本转语音参数
 */
@Data
public class ElevenLabsTtsParam {

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
    private TtsInput input;

    @Data
    public static class TtsInput {
        private String text;
        private String voice;
        private Double stability;

        @JsonProperty("similarity_boost")
        private Double similarityBoost;

        private Integer style;
        private Double speed;
        private Boolean timestamps;

        @JsonProperty("previous_text")
        private String previousText;

        @JsonProperty("next_text")
        private String nextText;

        @JsonProperty("language_code")
        private String languageCode;
    }

    /**
     * 文本转语音结果
     */
    @Data
    public static class TtsResult {
        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private String[] resultUrls;
    }
}