package com.fuse.ai.server.web.model.dto.request.video;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * Luma video generation request DTO
 */
@Data
public class LumaGenerateDTO {

    /**
     * Text prompt describing the desired video modifications
     */
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;

    /**
     * Input video file for modification
     */
    @NotNull(message = "Video file cannot be null")
    private MultipartFile videoFile;

    /**
     * Watermark identifier to add to generated video
     */
    private String watermark;

}