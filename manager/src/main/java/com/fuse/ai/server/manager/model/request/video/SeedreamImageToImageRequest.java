package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Seedream 图生图请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeedreamImageToImageRequest extends SeedreamBaseRequest {
    private ImageToImageInput input;

    @Data
    public static class ImageToImageInput {
        private String prompt;

        @JsonProperty("image_urls")
        private List<String> imageUrls;

        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        private String quality;
    }
}