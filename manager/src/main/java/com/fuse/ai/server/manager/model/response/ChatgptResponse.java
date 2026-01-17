package com.fuse.ai.server.manager.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatgptResponse {
    private String id;

    private String object;
    private List<Choice> choices;
    private String model;
    private Usage usage;

    // 判断是否是内容chunk
    public boolean isContentChunk() {
        return choices != null &&
                !choices.isEmpty() &&
                choices.get(0).delta != null &&
                choices.get(0).delta.content != null;
    }

    // 判断是否结束
    public boolean isFinished() {
        return choices != null &&
                !choices.isEmpty() &&
                "stop".equals(choices.get(0).finishReason);
    }

    // 获取内容
    public String getContent() {
        if (isContentChunk()) {
            return choices.get(0).delta.content;
        }
        return null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Delta delta;

        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Delta {
        private String content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
        @JsonProperty("input_tokens")
        private Integer inputTokens;
        @JsonProperty("output_tokens")
        private Integer outputTokens;
        @JsonProperty("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;
        @JsonProperty("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;
        @JsonProperty("input_tokens_details")
        private Object inputTokensDetails;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromptTokensDetails {
        @JsonProperty("cached_tokens")
        private Integer cachedTokens;
        @JsonProperty("audio_tokens")
        private Integer audioTokens;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionTokensDetails {
        @JsonProperty("reasoning_tokens")
        private Integer reasoningTokens;
        @JsonProperty("audio_tokens")
        private Integer audioTokens;
        @JsonProperty("accepted_prediction_tokens")
        private Integer acceptedPredictionTokens;
        @JsonProperty("rejected_prediction_tokens")
        private Integer rejectedPredictionTokens;
    }
}