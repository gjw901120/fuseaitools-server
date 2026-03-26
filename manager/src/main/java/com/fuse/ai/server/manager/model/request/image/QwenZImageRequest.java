package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.model.request.image.QwenBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QwenZImageRequest extends QwenBaseRequest {
    private ZImageInput input;

    @Data
    public static class ZImageInput {
        private String prompt;

        @JsonProperty("aspect_ratio")
        private String aspectRatio;
    }
}