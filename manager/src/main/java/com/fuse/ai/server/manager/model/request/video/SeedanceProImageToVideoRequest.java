package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeedanceProImageToVideoRequest extends SeedanceBaseRequest {
    private ProImageToVideoInput input;

    @Data
    public static class ProImageToVideoInput {
        private String prompt;

        @JsonProperty("image_url")
        private String imageUrl;

        private String resolution;
        private String duration;

        @JsonProperty("camera_fixed")
        private Boolean cameraFixed;

        private Integer seed;

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;
    }
}