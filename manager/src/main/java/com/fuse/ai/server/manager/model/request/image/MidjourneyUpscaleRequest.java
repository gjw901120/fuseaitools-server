package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Midjourney upscale request model
 * Feign client request model for Midjourney upscale API
 * 用于Midjourney图像放大处理的Feign客户端请求模型
 */
@Data
public class MidjourneyUpscaleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Task ID returned from MJ generation task
     * 从MJ生成任务返回的任务ID
     */
    @NotBlank(message = "Task ID cannot be empty")
    private String taskId;

    /**
     * Image index, range (0, 1, 2, 3) for the 4 generated images
     * 图像索引，对应4个生成图像中的位置，范围0-3
     * Note: This uses 0-based indexing (0, 1, 2, 3) instead of 1-based (1, 2, 3, 4)
     * 注意：这里使用0-based索引（0, 1, 2, 3），而不是1-based（1, 2, 3, 4）
     */
    @Range(min = 0, max = 3, message = "Image index must be between 0 and 3")
    private Integer imageIndex;

    /**
     * Watermark identifier (optional)
     * 水印标识符（可选）
     */
    @Size(max = 50, message = "Watermark cannot exceed 50 characters")
    private String waterMark;

    /**
     * Callback URL to receive task completion updates (optional)
     * 接收任务完成更新的回调URL（可选）
     */
    @URL(message = "Callback URL must be a valid URL")
    private String callBackUrl;

}
