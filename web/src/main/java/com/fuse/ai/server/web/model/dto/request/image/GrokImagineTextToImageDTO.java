package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Grok Imagine 文本生成图像请求 DTO
 * 模型: grok-imagine/text-to-image
 */
@Data
public class GrokImagineTextToImageDTO {

    /**
     * 模型名称，固定为 grok-imagine/text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "grok-imagine-text-to-image", message = "Model must be grok-imagine-text-to-image")
    private String model;

    /**
     * 描述期望图像的文本提示
     * 最大长度：5000 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 5000, message = "Prompt cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 指定生成图像的宽高比
     * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
     * 默认值：1:1
     */
    @Pattern(regexp = "2:3|3:2|1:1|16:9|9:16",
            message = "Aspect ratio must be one of: 2:3, 3:2, 1:1, 16:9, 9:16")
    private String aspectRatio;
}