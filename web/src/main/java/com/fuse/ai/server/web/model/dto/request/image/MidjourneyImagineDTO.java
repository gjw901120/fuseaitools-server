package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Request DTO for triggering Midjourney /imagine command
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MidjourneyImagineDTO {

    /**
     * Task type for generation mode
     * Options: mj_txt2img (Text-to-image), mj_img2img
     */
    @NotBlank(message = "Task type cannot be empty")
    @Pattern(regexp = "^(mj_txt2img|mj_img2img)$", message = "Task type must be mj_txt2img or mj_img2img")
    @JsonProperty("taskType")
    private String taskType = "mj_txt2img";

    /**
     * Text prompt describing the desired image content
     * Should be detailed and specific in describing image content
     * Can include style, composition, lighting and other visual elements
     * Max length: 2000 characters
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(max = 2000, message = "Prompt length cannot exceed 2000 characters")
    @JsonProperty("prompt")
    private String prompt;

    /**
     * Generation speed mode
     * Can be "fast", "relaxed" or "turbo"
     */
    @Pattern(regexp = "^(fast|relaxed|turbo)?$", message = "Speed must be fast, relaxed or turbo")
    @JsonProperty("speed")
    private String speed = "relaxed";

    /**
     * Input image URL array (required for image-to-image generation)
     * Use either fileUrl or fileUrls field
     * When generating videos, fileUrls can only have one image link
     */
    @JsonProperty("fileUrls")
    private List<String> fileUrls;

    /**
     * Output image/video aspect ratio
     * Available options: "1:2", "9:16", "2:3", "3:4", "5:6", "6:5", "4:3", "3:2", "1:1", "16:9", "2:1"
     */
    @Pattern(regexp = "^(1:2|9:16|2:3|3:4|5:6|6:5|4:3|3:2|1:1|16:9|2:1)?$",
            message = "Aspect ratio must be one of: 1:2, 9:16, 2:3, 3:4, 5:6, 6:5, 4:3, 3:2, 1:1, 16:9, 2:1")
    @JsonProperty("aspectRatio")
    private String aspectRatio = "1:1";

    /**
     * Midjourney model version to use
     * Available options: "7", "6.1", "6", "5.2", "5.1", "niji6", "niji7"
     */
    @Pattern(regexp = "^(7|6\\.1|6|5\\.2|5\\.1|niji6|niji7)?$",
            message = "Version must be one of: 7, 6.1, 6, 5.2, 5.1, niji6, niji7")
    @JsonProperty("version")
    private String version = "7";

    /**
     * Controls the diversity of generated images
     * Increment by 5 each time, such as (0, 5, 10, 15...)
     * Higher values create more diverse results
     * Lower values create more consistent results
     * Range: 0-100
     */
    @Range(min = 0, max = 100, message = "Variety must be between 0 and 100")
    @JsonProperty("variety")
    private Integer variety = 0;

    /**
     * Stylization level (0-1000)
     * Controls the artistic style intensity
     * Higher values create more stylized results
     * Lower values create more realistic results
     * Suggested to be a multiple of 50
     */
    @Range(min = 0, max = 1000, message = "Stylization must be between 0 and 1000")
    @JsonProperty("stylization")
    private Integer stylization = 100;

    /**
     * Weirdness level (0-3000)
     * Controls the creativity and uniqueness
     * Higher values create more unusual results
     * Lower values create more conventional results
     * Suggested to be a multiple of 100
     */
    @Range(min = 0, max = 3000, message = "Weirdness must be between 0 and 3000")
    @JsonProperty("weirdness")
    private Integer weirdness = 0;

    /**
     * Watermark identifier
     * Optional parameter. If provided, a watermark will be added to the generated content
     */
    @Size(max = 50, message = "Watermark length cannot exceed 50 characters")
    @JsonProperty("waterMark")
    private String waterMark;

    /**
     * Uploaded image file (alternative to fileUrls, for direct file upload)
     */
    private transient MultipartFile imageFile;

    /**
     * Single image URL (for backward compatibility)
     * Use fileUrls for multiple images
     */
    @JsonProperty("fileUrl")
    private String fileUrl;

    /**
     * Validate business rules for the request
     * 验证请求的业务规则
     */
    public void validateBusinessRules() {
        // Validate that image-to-image tasks require input images
        // 验证图像转图像任务需要输入图像
        if ("mj_img2img".equals(taskType)) {
            boolean hasInputImage = (fileUrls != null && !fileUrls.isEmpty()) ||
                    (fileUrl != null && !fileUrl.trim().isEmpty()) ||
                    (imageFile != null && !imageFile.isEmpty());
            if (!hasInputImage) {
                throw new IllegalArgumentException("Image-to-image tasks require input images");
            }
        }

        // Validate variety is multiple of 5
        // 验证多样性是5的倍数
        if (variety != null && variety % 5 != 0) {
            throw new IllegalArgumentException("Variety should be a multiple of 5 (0, 5, 10, 15...)");
        }

        // Validate stylization is multiple of 50 (suggestion)
        // 验证风格化是50的倍数（建议）
        if (stylization != null && stylization % 50 != 0) {
            // This is just a warning, not an error
            System.out.println("Warning: Stylization is suggested to be a multiple of 50");
        }

        // Validate weirdness is multiple of 100 (suggestion)
        // 验证怪异度是100的倍数（建议）
        if (weirdness != null && weirdness % 100 != 0) {
            // This is just a warning, not an error
            System.out.println("Warning: Weirdness is suggested to be a multiple of 100");
        }

        // Validate prompt length for specific versions
        // 为特定版本验证提示词长度
        if ("niji6".equals(version) || "niji7".equals(version)) {
            if (prompt.length() > 1000) {
                throw new IllegalArgumentException("Niji models have a maximum prompt length of 1000 characters");
            }
        }
    }

    /**
     * Check if this is a text-to-image task
     * 检查是否为文本转图像任务
     */
    public boolean isTextToImage() {
        return "mj_txt2img".equals(taskType);
    }

    /**
     * Check if this is an image-to-image task
     * 检查是否为图像转图像任务
     */
    public boolean isImageToImage() {
        return "mj_img2img".equals(taskType);
    }

    /**
     * Get the first image URL from fileUrls or fileUrl
     * 从fileUrls或fileUrl获取第一个图像URL
     */
    public String getFirstImageUrl() {
        if (fileUrls != null && !fileUrls.isEmpty()) {
            return fileUrls.get(0);
        }
        return fileUrl;
    }

    /**
     * Static factory method for creating text-to-image request
     * 创建文本转图像请求的静态工厂方法
     */
    public static MidjourneyImagineDTO createTextToImage(String prompt, String aspectRatio, String version) {
        MidjourneyImagineDTO dto = new MidjourneyImagineDTO();
        dto.setTaskType("mj_txt2img");
        dto.setPrompt(prompt);
        dto.setAspectRatio(aspectRatio);
        dto.setVersion(version);
        return dto;
    }

    /**
     * Static factory method for creating image-to-image request
     * 创建图像转图像请求的静态工厂方法
     */
    public static MidjourneyImagineDTO createImageToImage(String prompt, List<String> fileUrls,
                                                          String aspectRatio, String version) {
        MidjourneyImagineDTO dto = new MidjourneyImagineDTO();
        dto.setTaskType("mj_img2img");
        dto.setPrompt(prompt);
        dto.setFileUrls(fileUrls);
        dto.setAspectRatio(aspectRatio);
        dto.setVersion(version);
        return dto;
    }

    /**
     * Static factory method for creating advanced text-to-image request
     * 创建高级文本转图像请求的静态工厂方法
     */
    public static MidjourneyImagineDTO createAdvancedTextToImage(String prompt, String aspectRatio,
                                                                 String version, Integer variety,
                                                                 Integer stylization, Integer weirdness) {
        MidjourneyImagineDTO dto = new MidjourneyImagineDTO();
        dto.setTaskType("mj_txt2img");
        dto.setPrompt(prompt);
        dto.setAspectRatio(aspectRatio);
        dto.setVersion(version);
        dto.setVariety(variety);
        dto.setStylization(stylization);
        dto.setWeirdness(weirdness);
        return dto;
    }


    /**
     * Get human-readable description of the request
     * 获取请求的人类可读描述
     */
    public String getDescription() {
        return String.format("Midjourney %s request: %s (Aspect: %s, Version: %s)",
                taskType,
                prompt.length() > 50 ? prompt.substring(0, 47) + "..." : prompt,
                aspectRatio,
                version);
    }
}