package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.common.enums.VeoModelEnum;
import com.fuse.ai.server.web.common.enums.VeoGenerationTypeEnum;
import com.fuse.ai.server.web.common.enums.VeoAspectRatioEnum;
import com.fuse.ai.server.web.common.utils.FileValidatorUtil;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.simply.common.core.exception.BaseException;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.List;

/**
 * Veo视频生成请求参数
 */
@Data
public class VeoGenerateDTO {
    /**
     * 视频内容描述提示词
     * 必须详细且具体地描述视频内容，可以包含动作、场景、风格等信息
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 1, max = 1000, message = "Prompt length must be between 1-1000 characters")
    private String prompt;

    /**
     * 图片文件列表（图片生成视频模式使用）
     * 支持1张或2张图片：
     * - 1张图片：生成的视频围绕该图片展开
     * - 2张图片：第一张作为首帧，第二张作为尾帧
     */
    @Size(min = 0, max = 3, message = "Image count cannot exceed 3")
    private List<MultipartFile> imageFiles;

    /**
     * 模型类型
     */
    private String model = VeoModelEnum.VEO3_FAST.getCode();

    /**
     * 视频生成模式
     */
    private String generationType;

    /**
     * 水印文本
     */
    @Size(max = 50, message = "Watermark text cannot exceed 50 characters")
    private String watermark;

    /**
     * 视频宽高比
     */
    private String aspectRatio = VeoAspectRatioEnum.RATIO_16_9.getCode();

    /**
     * 随机种子
     * 取值范围：10000-99999
     */
    @Min(value = 10000, message = "Random seed minimum value is 10000")
    @Max(value = 99999, message = "Random seed maximum value is 99999")
    private Integer seeds;

    /**
     * 是否启用提示词翻译为英文
     */
    @NotNull(message = "Translation enable status cannot be empty")
    private Boolean enableTranslation = true;

    /**
     * 验证图片文件类型和大小 - 抛出异常方式
     */
    public void validateImageFilesWithException(long maxImageSize) {
        if (imageFiles == null || imageFiles.isEmpty()) {
            return; // No files to validate
        }

        FileValidatorUtil.validateImageFilesWithException(imageFiles, maxImageSize, 2);
    }

    /**
     * 验证图片文件类型和大小（使用默认大小）
     */
    public void validateImageFilesWithException() {
        // Default 10MB
        validateImageFilesWithException(10 * 1024 * 1024);
    }

    /**
     * 验证图片数量与生成模式的匹配
     */
    public void validateImageAndGenerationTypeWithException() {
        if (generationType == null) {
            throw new BaseException(ResponseErrorType.GENERATION_TYPE_ERROR,
                    "Invalid generation type");
        }

        VeoGenerationTypeEnum type = VeoGenerationTypeEnum.getByCode(generationType);
        if (type == null) {
            throw new BaseException(ResponseErrorType.GENERATION_TYPE_ERROR,
                    "Invalid generation type: " + generationType);
        }

        switch (type) {
            case REFERENCE_2_VIDEO:
                if (imageFiles == null || imageFiles.isEmpty() || imageFiles.size() > 3) {
                    throw new BaseException(ResponseErrorType.IMAGE_COUNT_ERROR,
                            "REFERENCE_2_VIDEO mode requires 1-3 images");
                }
                break;
            case FIRST_AND_LAST_FRAMES_2_VIDEO:
                if (imageFiles == null || !(imageFiles.size() == 1 || imageFiles.size() == 2)) {
                    throw new BaseException(ResponseErrorType.IMAGE_COUNT_ERROR,
                            "FIRST_AND_LAST_FRAMES_2_VIDEO mode requires exactly 1 or 2 images");
                }
                break;
            case TEXT_2_VIDEO:
                if (imageFiles != null && !imageFiles.isEmpty()) {
                    throw new BaseException(ResponseErrorType.IMAGE_COUNT_ERROR,
                            "TEXT_2_VIDEO mode should not have images");
                }
                break;
        }
    }

    /**
     * 验证模型与生成模式的兼容性
     */
    public void validateModelAndGenerationTypeWithException() {
        if (VeoGenerationTypeEnum.REFERENCE_2_VIDEO.getCode().equals(generationType)) {
            if (!VeoModelEnum.VEO3_FAST.getCode().equals(model)) {
                throw new BaseException(ResponseErrorType.MODEL_INCOMPATIBLE_ERROR);
            }
        }
    }

    /**
     * 验证宽高比限制
     */
    public void validateAspectRatioWithException() {
        if (VeoGenerationTypeEnum.REFERENCE_2_VIDEO.getCode().equals(generationType)) {
            if (!VeoAspectRatioEnum.RATIO_16_9.getCode().equals(aspectRatio)) {
                throw new BaseException(ResponseErrorType.ASPECT_RATIO_ERROR,
                        "REFERENCE_2_VIDEO mode only supports 16:9 aspect ratio");
            }
        }
    }

    /**
     * 获取模型枚举
     */
    public VeoModelEnum getModelEnum() {
        return VeoModelEnum.getByCode(model);
    }

    /**
     * 获取生成模式枚举
     */
    public VeoGenerationTypeEnum getGenerationTypeEnum() {
        return VeoGenerationTypeEnum.getByCode(generationType);
    }

    /**
     * 获取宽高比枚举
     */
    public VeoAspectRatioEnum getAspectRatioEnum() {
        return VeoAspectRatioEnum.getByCode(aspectRatio);
    }
}