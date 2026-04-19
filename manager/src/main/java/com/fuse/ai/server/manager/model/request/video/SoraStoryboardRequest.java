package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.enums.SoraAspectRatioEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class SoraStoryboardRequest {

    @JsonProperty("n_frames")
    private String nFrames;

    @JsonProperty("image_urls")
    private List<@URL(message = "Image Incorrect URL format") String> imageUrls;

    @JsonProperty("aspect_ratio")
    private SoraAspectRatioEnum aspectRatio;

    private List<SoraStoryboardSceneRequest> shots;
}