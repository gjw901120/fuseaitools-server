package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Nano Banana Pro image generation request DTO
 */
@Data
public class NanoBananaProGenerateDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "nano-banana-pro", message = "Model must be nano-banana-pro")
    private String model;

    /**
     * The prompt for image generation
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * List of input images
     */
    @NotNull(message = "Image files cannot be null")
    @Size(min = 1, max = 10, message = "Image files must contain 1 to 10 images")
    private List<String> imageInput;

    /**
     * resolution
     */
    @Pattern(regexp = "1K|2K|4K", message = "Resolution must be 1K,2K,4K")
    private String resolution;

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