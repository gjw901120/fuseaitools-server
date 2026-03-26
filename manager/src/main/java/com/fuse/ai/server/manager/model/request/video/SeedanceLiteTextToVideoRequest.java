package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeedanceLiteTextToVideoRequest extends SeedanceBaseRequest {
    private LiteTextToVideoInput input;

    @Data
    public static class LiteTextToVideoInput {
        private String prompt;

        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        private String resolution;
        private String duration;

        @JsonProperty("camera_fixed")
        private Boolean cameraFixed;

        private Integer seed;

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;
    }
}