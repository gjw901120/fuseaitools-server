package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * GPT-4o Image Generation request DTO
 */
@Data
public class Gpt4oImageGenerateDTO {

    /**
     * Image size aspect ratio
     */
    @NotBlank(message = "Size cannot be empty")
    @Pattern(regexp = "1:1|3:2|2:3", message = "Size must be 1:1, 3:2 or 2:3")
    private String size;

    /**
     * Image files list
     */
    @Size(max = 5, message = "Cannot exceed 5 image files")
    private List<String> imageUrls;

    /**
     * Prompt describing the desired content
     */
    private String prompt;

    /**
     * Prompt enhancement option
     */
    private Boolean isEnhance = false;

    /**
     * Number of image variants to generate
     */
    @Min(value = 1, message = "Number of variants must be 1, 2 or 4")
    @Max(value = 4, message = "Number of variants must be 1, 2 or 4")
    private Integer nVariants = 1;

    /**
     * Custom validation: either prompt or files must be provided
     */
    @AssertTrue(message = "Either prompt or files must be provided")
    public boolean isPromptOrFilesValid() {
        return (prompt != null && !prompt.trim().isEmpty()) ||
                (imageUrls != null && !imageUrls.isEmpty());
    }

}