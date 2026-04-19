package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.enums.SoraAspectRatioEnum;
import lombok.Data;

@Data
public class SoraInputBaseRequest {

    private String prompt;

    @JsonProperty("aspect_ratio")
    private SoraAspectRatioEnum aspectRatio;

    @JsonProperty("n_frames")
    private String nFrames;

    @JsonProperty("remove_watermark")
    private Boolean removeWatermark;
}