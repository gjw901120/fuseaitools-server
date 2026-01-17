package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaudeRequest {
    private String model;
    private Integer max_tokens;
    private List<Message> messages;
    private Boolean stream = true;
    private List<Tool> tools;

    @JsonProperty("stream_options")
    private StreamOptions streamOptions;  // 新增字段

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        private String role;
        private List<ContentItem> content;

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContentItem {
        private String type; // "text" 或 "image"
        private String text; // 当type为"text"时
        private Source source; // 当type为"image"时

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Source {
        private String type; // "url" 或其他类型
        private String url;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tool {
        private String type; // "web_search_20250305" 等
        private String name; // "web_search"
        private Integer max_uses;
        private Map<String, Object> parameters; // 可选参数
    }

    @Data
    public static class StreamOptions {

        @JsonProperty("include_usage")
        private Boolean includeUsage ; // 是否包含使用统计

    }
}