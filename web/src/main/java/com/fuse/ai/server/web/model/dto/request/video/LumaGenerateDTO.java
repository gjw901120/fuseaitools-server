package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Luma video generation request DTO
 */
@Data
public class LumaGenerateDTO {

    /**
     * Text prompt describing the desired video modifications
     */
    @NotBlank(message = "Prompt cannot be empty")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * Input video file for modification
     */
    @NotNull(message = "Video url cannot be null")
    private String videoUrl;

    /**
     * Watermark identifier to add to generated video
     */
    private String watermark;

}