package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Seedream 文生图请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeedreamTextToImageRequest extends SeedreamBaseRequest {
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {
        private String prompt;

        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        private String quality;
    }
}