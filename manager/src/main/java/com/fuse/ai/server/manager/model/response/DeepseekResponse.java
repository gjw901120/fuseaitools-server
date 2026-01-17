package com.fuse.ai.server.manager.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepseekResponse {

    private String id;

    private String object;

    private Long created;

    private String model;

    private List<Choice> choices;

    private Usage usage;

    private List<SearchResult> searchResults;

    private Boolean isFinal;

    private String finishReason;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Integer index;
        private Delta delta;
        private String finishReason;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Delta {
            private String content;
            private String role;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {

        private Integer promptTokens;

        private Integer completionTokens;

        private Integer totalTokens;

        private Map<String, Object> promptTokensDetails;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResult {
        private Integer index;
        private String url;
        private String title;
    }
}