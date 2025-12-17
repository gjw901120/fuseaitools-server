package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ElevenLabs音效生成参数
 */
@Data
public class ElevenLabsSoundEffectParam {

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
    private SoundEffectInput input;

    @Data
    public static class SoundEffectInput {
        private String text;
        private Boolean loop;

        @JsonProperty("prompt_influence")
        private Double promptInfluence;

        @JsonProperty("output_format")
        private String outputFormat;
    }

    /**
     * 音效生成结果
     */
    @Data
    public static class SoundEffectResult {
        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private String[] resultUrls;
    }
}