package com.fuse.ai.server.web.model.dto.request.video;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * Sora Pro Storyboard request DTO
 */
@Data
public class SoraProStoryboardDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "sora-2-pro-storyboard",
            message = "Model must be sora-2-pro-storyboard")
    private String model;

    /**
     * Callback URL for task completion notifications
     */
    private String callBackUrl;

    /**
     * Total length of the video
     */
    @NotBlank(message = "Number of frames cannot be empty")
    @Pattern(regexp = "10|15|25", message = "Number of frames must be 10, 15 or 25")
    private String nFrames;

    /**
     * Image files to use as input
     */
    private List<MultipartFile> imageFiles;

    /**
     * Aspect ratio of the image
     */
    @Pattern(regexp = "portrait|landscape", message = "Aspect ratio must be portrait or landscape")
    private String aspectRatio;

    /**
     * Array of scene objects defining the storyboard sequence
     */
    @NotNull(message = "Shots cannot be null")
    @Size(min = 1, message = "At least one shot is required")
    private List<Shot> shots;

    /**
     * Shot object with duration and scene description
     */
    @Data
    public static class Shot {

        /**
         * Duration in seconds
         */
        @NotNull(message = "Duration cannot be null")
        @Positive(message = "Duration must be positive")
        private Double duration;

        /**
         * Scene description/prompt
         */
        @NotBlank(message = "Scene description cannot be empty")
        private String scene;
    }

    /**
     * Custom validation for total duration matching nFrames
     */
    @AssertTrue(message = "Total shots duration must match nFrames value")
    public boolean isDurationValid() {
        if (getShots() == null || getNFrames() == null) {
            return false;
        }

        double totalDuration = getShots().stream()
                .mapToDouble(Shot::getDuration)
                .sum();

        int nFrames = Integer.parseInt(getNFrames());
        return Math.abs(totalDuration - nFrames) < 0.1; // Allow small floating point difference
    }

    /**
     * Custom validation for image file types and size
     */
    @AssertTrue(message = "Image files must be JPEG, PNG, or WebP and under 10MB")
    public boolean isImageFilesFormatValid() {
        if (getImageFiles() != null) {
            return getImageFiles().stream()
                    .allMatch(file -> file.isEmpty() ||
                            (file.getContentType() != null &&
                                    (file.getContentType().equals("image/jpeg") ||
                                            file.getContentType().equals("image/png") ||
                                            file.getContentType().equals("image/webp")) &&
                                    file.getSize() <= 10 * 1024 * 1024)); // 10MB
        }
        return true;
    }
}