package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Qwen2 文本生成图像请求 DTO
 * 模型: qwen2/text-to-image
 */
@Data
public class Qwen2TextToImageDTO {

    /**
     * 模型名称，固定为 qwen2-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "qwen2-text-to-image", message = "Model must be qwen2-text-to-image")
    private String model;

    /**
     * 用于图像生成的文本提示词
     * 最大长度：800 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 800, message = "Prompt cannot exceed 800 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 生成图像的尺寸规格
     * 可选值：1:1, 3:4, 4:3, 9:16, 16:9
     * 默认值：16:9
     */
    @Pattern(regexp = "1:1|3:4|4:3|9:16|16:9",
            message = "Image size must be one of: 1:1, 3:4, 4:3, 9:16, 16:9")
    private String imageSize;

    /**
     * 随机种子值
     */
    private Integer seed;

    /**
     * 生成图像的输出格式
     * 可选值：jpeg, png
     * 默认值：png
     */
    @Pattern(regexp = "jpeg|png", message = "Output format must be jpeg or png")
    private String outputFormat;
}