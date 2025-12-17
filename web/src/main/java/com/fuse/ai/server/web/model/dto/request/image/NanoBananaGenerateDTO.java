package com.fuse.ai.server.web.model.dto.request.image;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * Nano Banana image generation request DTO
 */
@Data
public class NanoBananaGenerateDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "google/nano-banana", message = "Model must be google/nano-banana")
    private String model;

    /**
     * The prompt for image generation
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * Output format for the images
     */
    @Pattern(regexp = "png|jpeg", message = "Output format must be png or jpeg")
    private String outputFormat;

    /**
     * Image size aspect ratio
     */
    @Pattern(regexp = "1:1|9:16|16:9|3:4|4:3|3:2|2:3|5:4|4:5|21:9|auto",
            message = "Image size must be 1:1, 9:16, 16:9, 3:4, 4:3, 3:2, 2:3, 5:4, 4:5, 21:9 or auto")
    private String imageSize;

}