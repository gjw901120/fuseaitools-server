package com.fuse.ai.server.manager.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * Claude流式响应基类
 * Claude API返回的是事件序列，每个事件都有type字段
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClaudeResponse.MessageStart.class, name = "message_start"),
        @JsonSubTypes.Type(value = ClaudeResponse.MessageDelta.class, name = "message_delta"),
        @JsonSubTypes.Type(value = ClaudeResponse.MessageStop.class, name = "message_stop"),
        @JsonSubTypes.Type(value = ClaudeResponse.ContentBlockStart.class, name = "content_block_start"),
        @JsonSubTypes.Type(value = ClaudeResponse.ContentBlockDelta.class, name = "content_block_delta"),
        @JsonSubTypes.Type(value = ClaudeResponse.ContentBlockStop.class, name = "content_block_stop"),
        @JsonSubTypes.Type(value = ClaudeResponse.Ping.class, name = "ping"),
        @JsonSubTypes.Type(value = ClaudeResponse.Error.class, name = "error")
})
@Data
public abstract class ClaudeResponse {

    private String type;

    // Message Start Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageStart extends ClaudeResponse {
        private ClaudeMessage message;

        public MessageStart() {
            super.type = "message_start";
        }
    }

    // Message Delta Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageDelta extends ClaudeResponse {
        private MessageDeltaData delta;
        private Usage usage;

        public MessageDelta() {
            super.type = "message_delta";
        }

        @Data
        public static class MessageDeltaData {
            @JsonProperty("stop_reason")
            private String stopReason;

            @JsonProperty("stop_sequence")
            private String stopSequence;
        }
    }

    // Message Stop Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageStop extends ClaudeResponse {
        public MessageStop() {
            super.type = "message_stop";
        }
    }

    // Content Block Start Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContentBlockStart extends ClaudeResponse {
        private Integer index;
        @JsonProperty("content_block")
        private ContentBlock contentBlock;

        public ContentBlockStart() {
            super.type = "content_block_start";
        }
    }

    // Content Block Delta Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContentBlockDelta extends ClaudeResponse {
        private Integer index;
        private Delta delta;

        public ContentBlockDelta() {
            super.type = "content_block_delta";
        }
    }

    // Content Block Stop Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContentBlockStop extends ClaudeResponse {
        private Integer index;

        public ContentBlockStop() {
            super.type = "content_block_stop";
        }
    }

    // Ping Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Ping extends ClaudeResponse {
        public Ping() {
            super.type = "ping";
        }
    }

    // Error Event
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error extends ClaudeResponse {
        private String error;

        public Error() {
            super.type = "error";
        }
    }

    // 自定义聚合事件（用于Service层处理）
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AggregatedEvent extends ClaudeResponse {
        private String content;
        private String delta;
        private Boolean isFinal;
        private Usage usage;
        private String stopReason;

        public AggregatedEvent() {
            super.type = "aggregated";
        }
    }

    // ========== 内部数据结构 ==========

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClaudeMessage {
        private String id;
        private String type;
        private String role;
        private String model;
        private List<ContentBlock> content;

        @JsonProperty("stop_reason")
        private String stopReason;

        @JsonProperty("stop_sequence")
        private String stopSequence;

        private Usage usage;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContentBlock {
        private String type;
        private String text;
        private List<String> image;
        private String source;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Delta {
        private String type;
        private String text;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Usage {
        @JsonProperty("input_tokens")
        private Integer inputTokens;

        @JsonProperty("output_tokens")
        private Integer outputTokens;

        @JsonProperty("cache_creation_input_tokens")
        private Integer cacheCreationInputTokens;

        @JsonProperty("cache_read_input_tokens")
        private Integer cacheReadInputTokens;

        @JsonProperty("cache_creation")
        private Map<String, Object> cacheCreation;

        @JsonProperty("server_tool_use")
        private Map<String, Object> serverToolUse;
    }
}