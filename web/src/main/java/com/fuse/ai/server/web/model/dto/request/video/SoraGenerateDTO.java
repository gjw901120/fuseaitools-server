package com.fuse.ai.server.web.model.dto.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Sora video generation request DTO
 */
@Data
public class SoraGenerateDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "sora-2-image-to-video|sora-2-text-to-video",
            message = "Model must be sora-2-image-to-video or sora-2-text-to-video")
    private String model;

    /**
     * The text prompt describing the desired video motion
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 10000, message = "Prompt cannot exceed 10000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * Image files to use as the first frame
     */
    private List<String> imageUrls;

    /**
     * Aspect ratio of the image
     */
    @Pattern(regexp = "portrait|landscape", message = "Aspect ratio must be portrait or landscape")
    private String aspectRatio;

    /**
     * The number of frames to be generated
     */
    @NotNull(message = "Number of frames cannot be null")
    @Pattern(regexp = "10|15", message = "Number of frames must be 10 or 15")
    @JsonProperty("nFrames")
    private String nFrames;

    /**
     * When enabled, removes watermarks from the generated video
     */
    private Boolean removeWatermark;


}