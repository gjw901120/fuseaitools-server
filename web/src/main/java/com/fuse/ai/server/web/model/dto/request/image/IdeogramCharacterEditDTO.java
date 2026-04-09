package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Ideogram 角色编辑请求 DTO
 * 模型示例: ideogram-character-edit
 */
@Data
public class IdeogramCharacterEditDTO {

    /**
     * 模型名称，例如 ideogram-character-edit
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    @Pattern(regexp = "^(http|https)://.*$", message = "Callback URL must be a valid URL")
    private String callBackUrl;

    /**
     * 提示词，用于填充遮罩部分，最大5000字符
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
     * 输入图片URL
     */
    @NotBlank(message = "Image URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String imageUrl;

    /**
     * 遮罩图片URL，需与输入图片尺寸匹配
     */
    @NotBlank(message = "Mask URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Mask URL must be a valid URL")
    private String maskUrl;

    /**
     * 角色参考图片URL列表，目前仅支持1张
     */
    @NotNull(message = "Reference image URLs cannot be null")
    @Size(min = 1, max = 1, message = "Exactly one reference image URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL") String> referenceImageUrls;

    /**
     * 渲染速度：TURBO, BALANCED, QUALITY
     */
    @Pattern(regexp = "TURBO|BALANCED|QUALITY", message = "Rendering speed must be TURBO, BALANCED, or QUALITY")
    private String renderingSpeed;

    /**
     * 风格：AUTO, REALISTIC, FICTION
     */
    @Pattern(regexp = "AUTO|REALISTIC|FICTION", message = "Style must be AUTO, REALISTIC, or FICTION")
    private String style;

    /**
     * 是否使用MagicPrompt扩展提示词
     */
    private Boolean expandPrompt;

    /**
     * 生成图片数量：1, 2, 3, 4
     */
    @Pattern(regexp = "1|2|3|4", message = "Number of images must be 1, 2, 3, or 4")
    private String numImages;

    /**
     * 随机数种子
     */
    private Integer seed;
}