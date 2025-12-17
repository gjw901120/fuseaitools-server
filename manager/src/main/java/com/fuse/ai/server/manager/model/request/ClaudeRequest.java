package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private StreamOptions stream_options;  // 新增字段

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StreamOptions {
        private Boolean include_usage;

        public static StreamOptions includeUsage() {
            StreamOptions options = new StreamOptions();
            options.setInclude_usage(true);
            return options;
        }
    }
}