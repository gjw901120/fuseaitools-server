package com.fuse.ai.server.web.model.dto.request.video;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * Sora watermark remover request DTO
 */
@Data
public class SoraWatermarkRemoverDTO {

    /**
     * The model name to use for generation
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "sora-watermark-remover",
            message = "Model must be sora-watermark-remover")
    private String model;

    /**
     * Video URL for watermark removal
     */
    @NotBlank(message = "Video URL cannot be empty")
    private String videoUrl;

}