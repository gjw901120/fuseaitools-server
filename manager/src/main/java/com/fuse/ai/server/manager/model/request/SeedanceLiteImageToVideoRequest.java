package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeedanceLiteImageToVideoRequest extends SeedanceBaseRequest {
    private LiteImageToVideoInput input;

    @Data
    public static class LiteImageToVideoInput {
        private String prompt;

        @JsonProperty("image_url")
        private String imageUrl;

        private String resolution;
        private String duration;

        @JsonProperty("camera_fixed")
        private Boolean cameraFixed;

        private Integer seed;

        @JsonProperty("end_image_url")
        private String endImageUrl;
    }
}