package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SoraWatermarkRemoverRequest {

    @JsonProperty("video_url")
    private String videoUrl;
}