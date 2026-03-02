package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QwenImageToImageRequest extends QwenBaseRequest {
    private ImageToImageInput input;

    @Data
    public static class ImageToImageInput {
        private String prompt;

        @JsonProperty("image_url")
        private String imageUrl;

        private Double strength;

        @JsonProperty("output_format")
        private String outputFormat;

        private String acceleration;

        @JsonProperty("negative_prompt")
        private String negativePrompt;

        private Integer seed;

        @JsonProperty("num_inference_steps")
        private Integer numInferenceSteps;

        @JsonProperty("guidance_scale")
        private Double guidanceScale;

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;
    }
}