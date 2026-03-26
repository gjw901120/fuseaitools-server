package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Midjourney vary request model
 * Feign client request model for Midjourney vary API
 * 用于Midjourney图像变体生成的Feign客户端请求模型
 */
@Data
public class MidjourneyVaryRequest implements Serializable {

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

    /**
     * Callback URL to receive task completion updates (optional)
     * 接收任务完成更新的回调URL（可选）
     */
    @URL(message = "Callback URL must be a valid URL")
    private String callBackUrl;

}
