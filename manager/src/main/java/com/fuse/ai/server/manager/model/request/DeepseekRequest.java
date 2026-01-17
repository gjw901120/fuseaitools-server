package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DeepseekRequest {
    private String model; // 模型ID，必需
    @JsonProperty("max_tokens")
    private Integer maxTokens; // 最大输出token数，可选
    private List<Message> messages; // 消息列表，必需
    private Double temperature; // 采样温度，可选
    private Boolean stream = true; // 是否流式输出，可选
    @JsonProperty("stream_options")
    private StreamOptions streamOptions; // 流式输出选项，可选
    private ThinkingConfig thinking; // 深度思考配置，可选

    @Data
    public static class Message {
        private String role; // user/system/assistant
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    public static class StreamOptions {

        @JsonProperty("include_usage")
        private Boolean includeUsage ; // 是否包含使用统计

    }

    @Data
    public static class ThinkingConfig {
        private String type; // enabled/disabled/auto
    }
}
