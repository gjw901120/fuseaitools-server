package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ElevenLabs语音转文本参数
 */
@Data
public class ElevenLabsSttParam {

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
    private SttInput input;

    @Data
    public static class SttInput {
        @JsonProperty("audio_url")
        private String audioUrl;

        @JsonProperty("language_code")
        private String languageCode;

        @JsonProperty("tag_audio_events")
        private Boolean tagAudioEvents;

        private Boolean diarize;
    }

    /**
     * 语音转文本结果
     */
    @Data
    public static class SttResult {
        /**
         * 结果对象
         */
        @JsonProperty("resultObject")
        private SttResultObject resultObject;

        @Data
        public static class SttResultObject {

            /**
             * 语言代码
             */
            @JsonProperty("language_code")
            private String languageCode;

            /**
             * 语言概率
             */
            @JsonProperty("language_probability")
            private Double languageProbability;

            /**
             * 转换后的文本
             */
            private String text;

            /**
             * 单词数组
             */
            private Word[] words;

            @Data
            public static class Word {

                /**
                 * 说话人ID
                 */
                @JsonProperty("speaker_id")
                private String speakerId;

                /**
                 * 开始时间
                 */
                private Double start;

                /**
                 * 结束时间
                 */
                private Double end;

                /**
                 * 文本内容
                 */
                private String text;

                /**
                 * 类型
                 */
                private String type;
            }
        }
    }
}