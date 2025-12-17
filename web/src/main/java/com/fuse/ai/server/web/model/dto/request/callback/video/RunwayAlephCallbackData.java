package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * RunwayAleph回调数据
 */
@Data
public class RunwayAlephCallbackData {
    @JsonProperty("result_video_url")
    private String resultVideoUrl;

    @JsonProperty("result_image_url")
    private String resultImageUrl;
}