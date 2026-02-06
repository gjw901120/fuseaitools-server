package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Midjourney vary request DTO
 * 用于Midjourney图像变体生成的请求DTO
 */
@Data
public class MidjourneyVaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Task ID returned from MJ generation task
     * 从MJ生成任务返回的任务ID
     */
    @NotBlank(message = "Task ID cannot be empty")
    private String taskId;

    /**
     * Image index, range (1, 2, 3, 4) for the 4 generated images
     * 图像索引，对应4个生成图像中的位置，范围1-4
     */
    @Range(min = 1, max = 4, message = "Image index must be between 1 and 4")
    private Integer imageIndex;

    /**
     * Watermark identifier (optional)
     * 水印标识符（可选）
     */
    @Size(max = 50, message = "Watermark cannot exceed 50 characters")
    private String waterMark;

}