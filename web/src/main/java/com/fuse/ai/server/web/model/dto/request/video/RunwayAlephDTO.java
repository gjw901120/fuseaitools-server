package com.fuse.ai.server.web.model.dto.request.video;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * Runway Aleph video transformation request DTO
 */
@Data
public class RunwayAlephDTO {

    /**
     * Descriptive text guiding how to transform the reference video
     */
    @NotBlank(message = "Prompt text cannot be empty")
    private String prompt;

    /**
     * Reference video file
     */
    @NotNull(message = "Video URL cannot be null")
    private MultipartFile videoUrl;

    /**
     * Optional watermark text displayed on the generated video
     */
    @Size(max = 20, message = "Watermark text cannot exceed 20 characters")
    private String waterMark;

    /**
     * Optional video aspect ratio
     */
    @Pattern(regexp = "16:9|9:16|4:3|3:4|1:1|21:9", message = "Aspect ratio must be 16:9, 9:16, 4:3, 3:4, 1:1, or 21:9")
    private String aspectRatio;

    /**
     * Optional random seed for reproducible results
     */
    @Min(value = 0, message = "Seed must be a positive integer")
    private Integer seed;

    /**
     * Optional reference image to influence output style or content
     */
    private MultipartFile referenceImageFile;
}