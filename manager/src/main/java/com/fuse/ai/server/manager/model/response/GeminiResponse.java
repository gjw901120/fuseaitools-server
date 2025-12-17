package com.fuse.ai.server.manager.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiResponse {

    /**
     * 响应ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 对象类型
     */
    @JsonProperty("object")
    private String object;

    /**
     * 创建时间戳
     */
    @JsonProperty("created")
    private Long created;

    /**
     * 模型名称
     */
    @JsonProperty("model")
    private String model;

    /**
     * 系统指纹
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 选择列表（响应块）
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    /**
     * Token 使用情况（只在最终块包含）
     */
    @JsonProperty("usage")
    private Usage usage;

    /**
     * 是否为最终块
     */
    private Boolean isFinal = false;

    /**
     * 完成原因
     */
    @JsonProperty("finish_reason")
    private String finishReason;

    // 内部类定义

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Choice {
        /**
         * 索引
         */
        @JsonProperty("index")
        private Integer index;

        /**
         * 增量内容
         */
        @JsonProperty("delta")
        private Delta delta;

        /**
         * 对数概率
         */
        @JsonProperty("logprobs")
        private Object logprobs;

        /**
         * 完成原因
         */
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Delta {
        /**
         * 角色
         */
        @JsonProperty("role")
        private String role;

        /**
         * 内容
         */
        @JsonProperty("content")
        private String content;

        /**
         * 推理内容（Gemini特有）
         */
        @JsonProperty("reasoning_content")
        private String reasoningContent;

        /**
         * 推理过程（Gemini特有）
         */
        @JsonProperty("reasoning")
        private String reasoning;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Usage {
        /**
         * 提示词Token数
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        /**
         * 完成Token数
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        /**
         * 总Token数
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;

        /**
         * 提示词Token详细分类
         */
        @JsonProperty("prompt_tokens_details")
        private TokenDetails promptTokensDetails;

        /**
         * 完成Token详细分类
         */
        @JsonProperty("completion_tokens_details")
        private TokenDetails completionTokensDetails;

        /**
         * 输入Token数（旧字段）
         */
        @JsonProperty("input_tokens")
        private Integer inputTokens;

        /**
         * 输出Token数（旧字段）
         */
        @JsonProperty("output_tokens")
        private Integer outputTokens;

        /**
         * 输入Token详细分类
         */
        @JsonProperty("input_tokens_details")
        private Map<String, Object> inputTokensDetails;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TokenDetails {
        /**
         * 缓存Token数
         */
        @JsonProperty("cached_tokens")
        private Integer cachedTokens;

        /**
         * 文本Token数
         */
        @JsonProperty("text_tokens")
        private Integer textTokens;

        /**
         * 音频Token数
         */
        @JsonProperty("audio_tokens")
        private Integer audioTokens;

        /**
         * 图像Token数
         */
        @JsonProperty("image_tokens")
        private Integer imageTokens;

        /**
         * 推理Token数（特有）
         */
        @JsonProperty("reasoning_tokens")
        private Integer reasoningTokens;
    }
}