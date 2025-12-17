package com.fuse.ai.server.web.model.dto.request.video;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Sora Pro video generation request DTO
 */
@Data
public class SoraProGenerateDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "sora-2-pro-image-to-video|sora-2-pro-text-to-video",
            message = "Model must be sora-2-pro-image-to-video or sora-2-pro-text-to-video")
    private String model;

    /**
     * The text prompt describing the desired video motion
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 10000, message = "Prompt cannot exceed 10000 characters")
    private String prompt;

    /**
     * URL of the image to use as the first frame
     */
    private List<MultipartFile> imageFiles;

    /**
     * Aspect ratio of the image
     */
    @Pattern(regexp = "portrait|landscape", message = "Aspect ratio must be portrait or landscape")
    private String aspectRatio;

    /**
     * The number of frames to be generated
     */
    @Pattern(regexp = "10|15", message = "Number of frames must be 10 or 15")
    private String nFrames;

    /**
     * The quality or size of the generated image
     */
    @Pattern(regexp = "standard|high", message = "Size must be standard or high")
    private String size;

    /**
     * When enabled, removes watermarks from the generated video
     */
    private Boolean removeWatermark;

    /**
     * Custom validation for imageUrls requirement based on model
     */
    @AssertTrue(message = "Image URLs are required for sora-2-pro-image-to-video model")
    public boolean isImageUrlsValid() {
        if ("sora-2-pro-text-to-video".equals(model)) {
            return getImageFiles() != null && !getImageFiles().isEmpty();
        }
        return true;
    }
}