package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.common.enums.FluxKontextAspectRatioEnum;
import com.fuse.ai.server.web.common.enums.FluxKontextModelEnum;
import com.fuse.ai.server.web.common.enums.FluxKontextOutputFormatEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Flux Kontext图像生成请求参数
 */
@Data
public class FluxKontextGenerateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需图像或编辑的文本提示词
     * 生成和编辑模式都需要
     * 应该详细且具体
     * 对于图像编辑，描述所需的更改
     * 对于图像生成，描述完整的场景
     * 重要：仅支持英文
     */
    @NotBlank(message = "The prompt cannot be empty")
    @Size(max = 5000, message = "The length of the prompt cannot exceed 5000 characters")
    private String prompt;

    /**
     * 是否启用自动翻译功能
     * 由于 prompt 仅支持英文，当此参数为 true 时，系统会自动将非英文的提示词翻译成英文
     * 如果您的提示词已经是英文，可设置为 false
     */
    private Boolean enableTranslation = true;

    /**
     * 编辑模式的输入图像
     * 编辑现有图像时需要
     */
    @NotNull(message = "The input image cannot be empty")
    private String imageUrl;

    /**
     * 输出图像的长宽比
     * 适用于文本到图像生成和图像编辑两种模式
     * 对于文本到图像生成，输出图像将遵循指定的长宽比
     * 对于图像编辑，如果提供了aspectRatio参数，编辑后的图像将遵循该比例
     * 如果未提供，图像将保持其原始长宽比
     */
    private FluxKontextAspectRatioEnum aspectRatio = FluxKontextAspectRatioEnum.RATIO_16_9;

    /**
     * 输出图像格式
     */
    private FluxKontextOutputFormatEnum outputFormat = FluxKontextOutputFormatEnum.JPEG;

    /**
     * 如果为 true，将对提示词进行增强处理
     * 可能会增加处理时间
     */
    private Boolean promptUpsampling = false;

    /**
     * 用于生成的模型版本
     */
    @NotNull(message = "The model cannot be empty")
    private FluxKontextModelEnum model = FluxKontextModelEnum.FLUX_KONTEXT_PRO;

    /**
     * 图像生成模式：输入和输出的审核级别
     * 值范围从 0（最严格）到 6（更宽松）
     * 图像编辑模式：输入和输出的审核级别
     * 值范围从 0（最严格）到 2（平衡）
     */
    private Integer safetyTolerance = 2;

    /**
     * 要添加到生成图像的水印标识符
     * 可选，如果提供，将在输出图像上添加水印
     */
    @Size(max = 100, message = "The length of the watermark identifier cannot exceed 100 characters")
    private String watermark;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {

        // 校验安全容忍度范围
        if (safetyTolerance < 0 || safetyTolerance > 6) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "The safety tolerance must be between 0 and 6");
        }


        // 校验水印长度
        if (watermark != null && watermark.length() > 100) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"The length of the watermark identifier cannot exceed 100 characters");
        }

        // 校验提示词长度
        if (prompt.length() > 5000) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"The length of the prompt word cannot exceed 5000 characters");
        }
    }

    /**
     * 判断是否为编辑模式
     */
    public boolean isEditMode() {
        return imageUrl != null && !imageUrl.isEmpty();
    }

    /**
     * 获取安全容忍度的有效范围
     * 编辑模式：0-2，生成模式：0-6
     */
    public int getSafetyToleranceMax() {
        return isEditMode() ? 2 : 6;
    }

    /**
     * 校验安全容忍度是否在有效范围内
     */
    public boolean isSafetyToleranceValid() {
        int max = getSafetyToleranceMax();
        return safetyTolerance >= 0 && safetyTolerance <= max;
    }
}