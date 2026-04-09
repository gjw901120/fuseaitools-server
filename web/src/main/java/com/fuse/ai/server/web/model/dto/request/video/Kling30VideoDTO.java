package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Kling 3.0 视频生成请求 DTO
 * 模型示例: kling-3.0-video
 */
@Data
public class Kling30VideoDTO {

    /**
     * 模型名称，例如 kling-3.0-video
     */
    @NotBlank(message = "Model cannot be empty")
    private String model;

    /**
     * 生成模式：std（标准）或 pro（专业）
     */
    @NotBlank(message = "Mode cannot be empty")
    @Pattern(regexp = "std|pro", message = "Mode must be std or pro")
    private String mode;

    /**
     * 图片URL列表
     * 单镜头：[start_frame_url, end_frame_url]
     * 多镜头：[start_frame_url]
     */
    @NotNull(message = "Image URLs cannot be null")
    @Size(min = 1, message = "At least one image URL is required")
    private List<@Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL") String> imageUrls;

    /**
     * 提示词（单镜头模式必填），最大2500字符
     */
    @Size(max = 2500, message = "Prompt cannot exceed 2500 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 多镜头提示词数组（多镜头模式必填）
     */
    private List<MultiPrompt> multiPrompt;

    /**
     * 视频时长，单镜头：3-15秒，多镜头：各镜头总时长3-15秒
     */
    @NotBlank(message = "Duration cannot be empty")
    @Pattern(regexp = "^([3-9]|1[0-5])$", message = "Duration must be between 3 and 15 seconds")
    private String duration;

    /**
     * 是否启用多镜头模式
     */
    @NotNull(message = "Multi shots cannot be null")
    private Boolean multiShots;

    /**
     * 是否启用声音效果
     */
    @NotNull(message = "Sound cannot be null")
    private Boolean sound;

    /**
     * Kling元素数组，可在提示词中引用
     */
    private List<KlingElement> klingElements;

    /**
     * 视频宽高比：16:9, 9:16, 1:1
     */
    @Pattern(regexp = "16:9|9:16|1:1", message = "Aspect ratio must be 16:9, 9:16, or 1:1")
    private String aspectRatio;

    /**
     * 多镜头提示词内部类
     */
    @Data
    public static class MultiPrompt {
        /**
         * 提示词，最大500字符
         */
        @NotBlank(message = "Prompt cannot be empty")
        @Size(max = 500, message = "Prompt cannot exceed 500 characters")
        private String prompt;

        /**
         * 时长，1-12秒
         */
        @NotNull(message = "Duration cannot be null")
        @Pattern(regexp = "^([1-9]|1[0-2])$", message = "Duration must be between 1 and 12 seconds")
        private Integer duration;

        private String elementName;
    }

    /**
     * Kling元素内部类
     */
    @Data
    public static class KlingElement {
        /**
         * 元素名称，用于@element_name引用
         */
        @NotBlank(message = "Element name cannot be empty")
        private String name;

        /**
         * 元素描述
         */
        private String description;

        /**
         * 元素图片URL列表，2-50张
         */
        @NotNull(message = "Element input URLs cannot be null")
        @Size(min = 2, max = 50, message = "Element input URLs must contain 2 to 50 images")
        private List<@Pattern(regexp = "^(http|https)://.*$", message = "Element URL must be a valid URL") String> elementInputUrls;
    }
}