package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Imagine 任务请求参数
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MidjourneyImagineRequest {

    private static final long serialVersionUID = 1L;

    /**
     * Task type for generation mode
     * Required field
     */
    @NotBlank(message = "Task type is required")
    @Pattern(regexp = "^(mj_txt2img|mj_img2img)$",
            message = "Task type must be one of: mj_txt2img, mj_img2img")
    private String taskType = "mj_txt2img";

    /**
     * Text prompt describing the desired image content
     * Required field, max 2000 characters
     */
    @NotBlank(message = "Prompt is required")
    @Size(max = 2000, message = "Prompt cannot exceed 2000 characters")
    private String prompt;

    /**
     * Generation speed mode
     * Optional, not required for mj_video or mj_omni_reference tasks
     */
    @Pattern(regexp = "^(fast|relaxed|turbo)?$",
            message = "Speed must be one of: fast, relaxed, turbo")
    private String speed = "relaxed";

    /**
     * Input image URL array
     * Use either fileUrl or fileUrls field
     * For video generation, fileUrls can only have one image link
     */
    private List<@URL(message = "File URL must be a valid URL") String> fileUrls;

    /**
     * Output image/video aspect ratio
     * Optional field
     */
    @Pattern(regexp = "^(1:2|9:16|2:3|3:4|5:6|6:5|4:3|3:2|1:1|16:9|2:1)?$",
            message = "Aspect ratio must be one of: 1:2, 9:16, 2:3, 3:4, 5:6, 6:5, 4:3, 3:2, 1:1, 16:9, 2:1")
    private String aspectRatio = "1:1";

    /**
     * Midjourney model version to use
     * Optional field
     */
    @Pattern(regexp = "^(7|6\\.1|6|5\\.2|5\\.1|niji6|niji7)?$",
            message = "Version must be one of: 7, 6.1, 6, 5.2, 5.1, niji6, niji7")
    private String version = "7";

    /**
     * Controls the diversity of generated images
     * Optional, range 0-100, should be multiple of 5
     */
    @Range(min = 0, max = 100, message = "Variety must be between 0 and 100")
    private Integer variety;

    /**
     * Stylization level (0-1000)
     * Optional, suggested to be multiple of 50
     */
    @Range(min = 0, max = 1000, message = "Stylization must be between 0 and 1000")
    private Integer stylization;

    /**
     * Weirdness level (0-3000)
     * Optional, suggested to be multiple of 100
     */
    @Range(min = 0, max = 3000, message = "Weirdness must be between 0 and 3000")
    private Integer weirdness;


    /**
     * Watermark identifier
     * Optional parameter
     */
    @Size(max = 50, message = "Watermark cannot exceed 50 characters")
    private String waterMark;

    /**
     * Callback URL to receive task completion updates
     * Optional but recommended for production use
     */
    @URL(message = "Callback URL must be a valid URL")
    private String callBackUrl;
}