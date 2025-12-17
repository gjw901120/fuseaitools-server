package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiRequest {

    /**
     * 模型ID
     * 示例："gemini-3-pro-preview"
     */
    @JsonProperty("model")
    private String model;

    /**
     * 消息列表
     */
    @JsonProperty("messages")
    private List<Message> messages;

    /**
     * 温度参数
     */
    @JsonProperty("temperature")
    private Double temperature;

    /**
     * Top-p 采样参数
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 是否流式输出
     */
    @JsonProperty("stream")
    private Boolean stream = true;

    /**
     * 流式选项
     */
    @JsonProperty("stream_options")
    private StreamOptions streamOptions;

    // 内部类定义

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        /**
         * 角色：user, assistant
         */
        @JsonProperty("role")
        private String role;

        /**
         * 内容
         */
        @JsonProperty("content")
        private List<ContentItem> content;

    }

    @Data
    public static class ContentItem {
        protected String type;

        private String text;

        @JsonProperty("image_url")
        private imageUrl imageUrl;

        @Data
        public static class imageUrl {
            private String url;
        }
    }

    @Data
    public static class StreamOptions {
        /**
         * 是否包含使用情况统计
         */
        @JsonProperty("include_usage")
        private Boolean includeUsage = true;
    }

}