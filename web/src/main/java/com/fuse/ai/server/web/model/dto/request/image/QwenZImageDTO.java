package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Qwen Z-Image 请求 DTO
 */
@Data
public class QwenZImageDTO {

    /**
     * 模型名称，固定为 z-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "qwen-z-image", message = "Model must be qwen-z-image")
    private String model;

    /**
     * 图像生成提示词，最大1000字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 1000, message = "Prompt cannot exceed 1000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 图像宽高比
     */
    @NotBlank(message = "Aspect ratio cannot be empty")
    @Pattern(regexp = "1:1|4:3|3:4|16:9|9:16", message = "Aspect ratio must be one of: 1:1, 4:3, 3:4, 16:9, 9:16")
    private String aspectRatio;
}