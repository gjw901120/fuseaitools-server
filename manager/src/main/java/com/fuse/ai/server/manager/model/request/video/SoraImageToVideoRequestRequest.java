package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoraImageToVideoRequestRequest extends SoraInputBaseRequest {

    @JsonProperty("image_urls")
    private List<@URL(message = "Image Incorrect URL format") String> imageUrls;
}