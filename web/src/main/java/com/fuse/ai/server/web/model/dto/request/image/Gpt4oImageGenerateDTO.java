package com.fuse.ai.server.web.model.dto.request.image;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import lombok.Data;
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
     * Mask image file
     */
    private MultipartFile maskFile;

    /**
     * Image files list
     */
    @Size(max = 5, message = "Cannot exceed 5 image files")
    private List<MultipartFile> files;

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
                (files != null && !files.isEmpty());
    }

    /**
     * Custom validation: mask file cannot be used with multiple files
     */
    @AssertTrue(message = "Mask file cannot be used when multiple files are provided")
    public boolean isMaskFileValid() {
        if (maskFile != null && !maskFile.isEmpty()) {
            return files == null || files.size() <= 1;
        }
        return true;
    }

    /**
     * Custom validation: mask file size validation
     */
    @AssertTrue(message = "Mask file must be under 25MB")
    public boolean isMaskFileSizeValid() {
        if (maskFile != null && !maskFile.isEmpty()) {
            return maskFile.getSize() <= 25 * 1024 * 1024;
        }
        return true;
    }

    /**
     * Custom validation: image files format validation
     */
    @AssertTrue(message = "Image files must be JFIF, JPEG, JPG, PNG, or WebP format")
    public boolean isImageFilesFormatValid() {
        if (files != null) {
            return files.stream()
                    .allMatch(file -> file.isEmpty() ||
                            (file.getContentType() != null &&
                                    (file.getContentType().equals("image/jpeg") ||
                                            file.getContentType().equals("image/jpg") ||
                                            file.getContentType().equals("image/pjpeg") ||
                                            file.getContentType().equals("image/jfif") ||
                                            file.getContentType().equals("image/pjp") ||
                                            file.getContentType().equals("image/png") ||
                                            file.getContentType().equals("image/webp"))));
        }
        return true;
    }
}