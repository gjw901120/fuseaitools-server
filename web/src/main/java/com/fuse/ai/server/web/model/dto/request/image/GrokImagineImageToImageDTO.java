package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Grok Imagine 图像生成图像请求 DTO
 * 模型: grok-imagine/image-to-image
 */
@Data
public class GrokImagineImageToImageDTO {

    /**
     * 模型名称，固定为 grok-imagine/image-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "grok-imagine-image-to-image", message = "Model must be grok-imagine-image-to-image")
    private String model;

    /**
     * 指定生成图像所需内容或样式的文本描述
     * 最大长度：390000 字符
     */
    @Size(max = 390000, message = "Prompt cannot exceed 390000 characters")
    private String prompt;

    /**
     * 参考图像的 URL 列表
     * 最多包含 5 个字符串
     */
    @NotBlank(message = "Image urls cannot be empty")
    private List<String> imageUrls;
}