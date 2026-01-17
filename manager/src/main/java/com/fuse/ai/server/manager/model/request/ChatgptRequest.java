package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatgptRequest {

    /**
     * 模型ID
     * 示例："gpt-4.1"
     */
    private String model;

    /**
     * 输入消息列表
     */
    private List<InputMessage> messages;

    /**
     * 工具定义数组
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tool> tools;

    /**
     * 推理控制配置
     */
    @JsonProperty("reasoning")
    private Reasoning reasoning;

    /**
     * 是否流式输出
     */
    @JsonProperty("stream")
    private Boolean stream;

    @JsonProperty("stream_options")
    private StreamOptions streamOptions;  // 新增字段

    @Data
    public static class StreamOptions {

        @JsonProperty("include_usage")
        private Boolean includeUsage; // 是否包含使用统计

    }

    // 内部类：输入消息
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InputMessage {

        /**
         * 角色：user, assistant, system
         */
        @JsonProperty("role")
        private String role;

        /**
         * 内容数组，可包含文本和图片
         */
        @JsonProperty("content")
        private List<ContentItem> content;
    }

    // 内容项基类（多态支持）
    @Data
    public static class ContentItem {
        protected String type;

        private String text;

        @JsonProperty("image_url")
        private ImageUrl imageUrl;

        @Data
        public static class ImageUrl {
            private String url;
        }
    }

    // 工具定义
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tool {
        /**
         * 工具类型
         * 示例："web_search_preview"
         */
        @JsonProperty("type")
        private String type;
    }

    // 推理配置
    @Data
    public static class Reasoning {
        /**
         * 推理强度：low, medium, high
         */
        @JsonProperty("effort")
        private String effort = "medium";
    }
}