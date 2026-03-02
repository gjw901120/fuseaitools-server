package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QwenTextToImageRequest extends QwenBaseRequest {
    private TextToImageInput input;

    @Data
    public static class TextToImageInput {
        private String prompt;

        @JsonProperty("image_size")
        private String imageSize;

        @JsonProperty("num_inference_steps")
        private Integer numInferenceSteps;

        private Integer seed;

        @JsonProperty("guidance_scale")
        private Double guidanceScale;

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;

        @JsonProperty("output_format")
        private String outputFormat;

        @JsonProperty("negative_prompt")
        private String negativePrompt;

        private String acceleration;
    }
}