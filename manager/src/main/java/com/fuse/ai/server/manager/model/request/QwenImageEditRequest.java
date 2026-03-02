package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QwenImageEditRequest extends QwenBaseRequest {
    private ImageEditInput input;

    @Data
    public static class ImageEditInput {
        private String prompt;

        @JsonProperty("image_url")
        private String imageUrl;

        private String acceleration;

        @JsonProperty("image_size")
        private String imageSize;

        @JsonProperty("num_inference_steps")
        private Integer numInferenceSteps;

        private Integer seed;

        @JsonProperty("guidance_scale")
        private Double guidanceScale;

        @JsonProperty("sync_mode")
        private Boolean syncMode;

        @JsonProperty("num_images")
        private String numImages;  // 文档中为字符串，可选1-4

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;

        @JsonProperty("output_format")
        private String outputFormat;

        @JsonProperty("negative_prompt")
        private String negativePrompt;
    }
}