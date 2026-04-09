package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Imagen 4 Ultra 图像生成请求 DTO
 * 模型: imagen4-ultra
 */
@Data
public class Imagen4UltraDTO {

    /**
     * 模型名称，固定为 imagen4-ultra
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "imagen4-ultra", message = "Model must be imagen4-ultra")
    private String model;

    /**
     * 用于描述生成图像内容的文本提示词
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
     * 用于描述生成图像中需要规避的元素的文本
     * 最大长度：5000 字符
     */
    @Size(max = 5000, message = "Negative prompt cannot exceed 5000 characters")
    private String negativePrompt;

    /**
     * 生成图像的宽高比
     * 可选值：1:1, 16:9, 9:16, 3:4, 4:3
     * 默认值：1:1
     */
    @Pattern(regexp = "1:1|16:9|9:16|3:4|4:3", message = "Aspect ratio must be one of: 1:1, 16:9, 9:16, 3:4, 4:3")
    private String aspectRatio;

    /**
     * 用于生成结果可复现的随机种子值
     * 最大长度：500 字符
     */
    @Size(max = 500, message = "Seed cannot exceed 500 characters")
    private String seed;
}