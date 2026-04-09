package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Nano Banana 2 图像生成请求 DTO
 * 模型: nano-banana-2
 */
@Data
public class NanoBanana2DTO {

    /**
     * 模型名称，固定为 nano-banana-2
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "nano-banana-2", message = "Model must be nano-banana-2")
    private String model;

    /**
     * 用于描述待生成图像的文本提示词
     * 最大长度：20000 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 20000, message = "Prompt cannot exceed 20000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 用于图像变换或作为参考的输入图像
     * 最多支持 14 张图片
     */
    private List<String> imageInput;

    /**
     * 生成图像的宽高比
     * 默认值：auto
     */
    @Pattern(regexp = "auto|1:1|1:4|16:9|1:8|21:9|2:3|3:2|3:4|4:1|4:3|4:5|5:4|8:1|9:16",
            message = "Aspect ratio must be a valid value")
    private String aspectRatio;

    /**
     * 生成图像的分辨率
     * 可选值：1K, 2K, 4K
     * 默认值：1K
     */
    @Pattern(regexp = "1K|2K|4K", message = "Resolution must be 1K, 2K, or 4K")
    private String resolution;

    /**
     * 生成图像的输出格式
     * 可选值：jpg, png
     * 默认值：jpg
     */
    @Pattern(regexp = "jpg|png", message = "Output format must be jpg or png")
    private String outputFormat;
}