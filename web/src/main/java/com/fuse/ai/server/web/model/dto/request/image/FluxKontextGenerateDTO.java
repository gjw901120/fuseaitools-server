package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.common.enums.FluxKontextAspectRatioEnum;
import com.fuse.ai.server.web.common.enums.FluxKontextModelEnum;
import com.fuse.ai.server.web.common.enums.FluxKontextOutputFormatEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    @NotBlank(message = "提示词不能为空")
    @Size(max = 5000, message = "提示词长度不能超过5000个字符")
    private String prompt;

    /**
     * 是否启用自动翻译功能
     * 由于 prompt 仅支持英文，当此参数为 true 时，系统会自动将非英文的提示词翻译成英文
     * 如果您的提示词已经是英文，可设置为 false
     */
    @NotNull(message = "翻译启用状态不能为空")
    private Boolean enableTranslation = true;

    /**
     * 编辑模式的输入图像
     * 编辑现有图像时需要
     */
    private MultipartFile inputImage;

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
    @NotNull(message = "提示词增强状态不能为空")
    private Boolean promptUpsampling = false;

    /**
     * 用于生成的模型版本
     */
    @NotNull(message = "模型不能为空")
    private FluxKontextModelEnum model = FluxKontextModelEnum.FLUX_KONTEXT_PRO;

    /**
     * 图像生成模式：输入和输出的审核级别
     * 值范围从 0（最严格）到 6（更宽松）
     * 图像编辑模式：输入和输出的审核级别
     * 值范围从 0（最严格）到 2（平衡）
     */
    @NotNull(message = "安全容忍度不能为空")
    private Integer safetyTolerance = 2;

    /**
     * 要添加到生成图像的水印标识符
     * 可选，如果提供，将在输出图像上添加水印
     */
    @Size(max = 100, message = "水印标识符长度不能超过100个字符")
    private String watermark;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 校验安全容忍度范围
        if (safetyTolerance < 0 || safetyTolerance > 6) {
            throw new IllegalArgumentException("安全容忍度必须在0-6之间");
        }

        // 如果是编辑模式，必须有输入图片
        if (inputImage != null && !inputImage.isEmpty()) {
            validateInputImage();
        }

        // 校验水印长度
        if (watermark != null && watermark.length() > 100) {
            throw new IllegalArgumentException("水印标识符长度不能超过100个字符");
        }

        // 校验提示词长度
        if (prompt.length() > 5000) {
            throw new IllegalArgumentException("提示词长度不能超过5000个字符");
        }
    }

    /**
     * 校验输入图片
     */
    private void validateInputImage() {
        if (inputImage.isEmpty()) {
            throw new IllegalArgumentException("输入图片不能为空");
        }

        // 校验文件类型
        String contentType = inputImage.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("输入文件必须是图片格式");
        }

        // 校验文件大小（例如限制为10MB）
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (inputImage.getSize() > maxSize) {
            throw new IllegalArgumentException("输入图片大小不能超过10MB");
        }
    }

    /**
     * 判断是否为编辑模式
     */
    public boolean isEditMode() {
        return inputImage != null && !inputImage.isEmpty();
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