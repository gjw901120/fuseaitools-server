package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * GPT Image 2 文生图请求 DTO
 * 模型端点固定使用: gpt-image-2-text-to-image
 */
@Data
public class GptImageV2TextToImageDTO {

    /**
     * 模型名称，必须使用 gpt-image-2-text-to-image
     */
    @NotBlank(message = "Model cannot be empty")
    private String model = "gpt-image-2-text-to-image";

    /**
     * 文本提示词，必填，最多20000个字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 20000, message = "Prompt cannot exceed 20000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 生成图片的比例，默认auto
     * 允许值：auto、1:1、9:16、16:9、4:3、3:4
     */
    @Pattern(regexp = "auto|1:1|9:16|16:9|4:3|3:4", message = "Aspect ratio must be one of: auto, 1:1, 9:16, 16:9, 4:3, 3:4")
    private String aspectRatio = "auto";

    /**
     * 图片的分辨率
     * 允许值：1K、2K、4K
     * 注意：1:1比例的图片无法生成4K图片
     */
    @Pattern(regexp = "1K|2K|4K", message = "Resolution must be one of: 1K, 2K, 4K")
    private String resolution;
}