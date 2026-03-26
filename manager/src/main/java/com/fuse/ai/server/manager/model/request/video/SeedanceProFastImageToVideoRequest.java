package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeedanceProFastImageToVideoRequest extends SeedanceBaseRequest {
    private ProFastImageToVideoInput input;

    @Data
    public static class ProFastImageToVideoInput {
        private String prompt;

        @JsonProperty("image_url")
        private String imageUrl;

        private String resolution;
        private String duration;
    }
}