package com.fuse.ai.server.web.model.dto.request.video;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * Runway video generation request DTO
 */
@Data
public class RunwayGenerateDTO {

    /**
     * Descriptive text guiding AI video generation
     */
    @NotBlank(message = "Prompt text cannot be empty")
    @Size(max = 1800, message = "Prompt text cannot exceed 1800 characters")
    private String prompt;

    /**
     * Video duration, options: 5 or 10 seconds
     */
    @NotNull(message = "Duration cannot be null")
    @Min(value = 5, message = "Duration must be 5 or 10 seconds")
    @Max(value = 10, message = "Duration must be 5 or 10 seconds")
    private Integer duration;

    /**
     * Video quality, options: 720p or 1080p
     */
    @NotBlank(message = "Quality cannot be empty")
    @Pattern(regexp = "720p|1080p", message = "Quality must be 720p or 1080p")
    private String quality;

    /**
     * Image file as video base
     */
    private MultipartFile imageFile;

    /**
     * Video aspect ratio parameter
     */
    @Pattern(regexp = "16:9|4:3|1:1|3:4|9:16", message = "Aspect ratio must be 16:9, 4:3, 1:1, 3:4, or 9:16")
    private String aspectRatio;

    /**
     * Video watermark text content
     */
    private String waterMark;

    /**
     * Custom validation for duration and quality constraints
     */
    @AssertTrue(message = "1080p quality is not supported for 10-second videos")
    public boolean isDurationAndQualityValid() {
        if (duration == null || quality == null) {
            return true;
        }
        return !(duration == 10 && "1080p".equals(quality));
    }

    /**
     * Custom validation for aspect ratio requirement
     */
    @AssertTrue(message = "Aspect ratio is required when no image file is provided")
    public boolean isAspectRatioValid() {
        if (imageFile == null || imageFile.isEmpty()) {
            return aspectRatio != null && !aspectRatio.trim().isEmpty();
        }
        return true;
    }
}