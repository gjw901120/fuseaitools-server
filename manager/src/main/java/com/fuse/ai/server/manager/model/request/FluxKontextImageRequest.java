// FluxKontextImageRequest.java
package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.FluxKontextConstant;
import com.fuse.ai.server.manager.enums.FluxKontextAspectRatioEnum;
import com.fuse.ai.server.manager.enums.FluxKontextModelEnum;
import com.fuse.ai.server.manager.enums.FluxKontextOutputFormatEnum;
import com.fuse.ai.server.manager.enums.FluxKontextSafetyToleranceEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Flux Kontext图像生成/编辑请求参数
 */
@Data
public class FluxKontextImageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需图像或编辑的文本提示词
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = 2000, message = "提示词长度不能超过2000个字符")
    private String prompt;

    /**
     * 是否启用自动翻译功能
     */
    private Boolean enableTranslation = true;

    /**
     * 上传服务器区域
     */
    private Boolean uploadCn = false;

    /**
     * 编辑模式的输入图像URL
     */
    @URL(message = "输入图像URL格式不正确")
    private String inputImage;

    /**
     * 输出图像的长宽比
     */
    @NotNull(message = "长宽比不能为空")
    private FluxKontextAspectRatioEnum aspectRatio = FluxKontextAspectRatioEnum.RATIO_16_9;

    /**
     * 输出图像格式
     */
    @NotNull(message = "输出格式不能为空")
    private FluxKontextOutputFormatEnum outputFormat = FluxKontextOutputFormatEnum.JPEG;

    /**
     * 是否对提示词进行增强处理
     */
    private Boolean promptUpsampling = false;

    /**
     * 用于生成的模型版本
     */
    @NotNull(message = "模型版本不能为空")
    private FluxKontextModelEnum model = FluxKontextModelEnum.FLUX_KONTEXT_PRO;

    /**
     * 回调URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 安全容忍度
     */
    @NotNull(message = "安全容忍度不能为空")
    private FluxKontextSafetyToleranceEnum safetyTolerance = FluxKontextSafetyToleranceEnum.LEVEL_2;

    /**
     * 水印标识符
     */
    @Size(max = 50, message = "水印标识符长度不能超过50个字符")
    private String watermark;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 验证提示词语言
        validatePromptLanguage();

        // 验证安全容忍度范围
        validateSafetyTolerance();

        // 验证输入图像URL（编辑模式）
        validateInputImageForEditMode();
    }

    /**
     * 验证提示词语言
     */
    private void validatePromptLanguage() {
        if (prompt != null && !prompt.trim().isEmpty()) {
            // 如果禁用翻译，检查提示词是否为英文
            if (Boolean.FALSE.equals(enableTranslation)) {
                // 简单检查是否包含中文字符
                if (prompt.matches(".*[\\u4e00-\\u9fa5]+.*")) {
                    throw new IllegalArgumentException("禁用翻译时提示词必须为英文");
                }
            }
        }
    }

    /**
     * 验证安全容忍度范围
     */
    private void validateSafetyTolerance() {
        if (safetyTolerance != null) {
            int maxLevel = isEditMode() ?
                    FluxKontextConstant.SAFETY_TOLERANCE_MAX_EDIT :
                    FluxKontextConstant.SAFETY_TOLERANCE_MAX_GENERATE;

            if (safetyTolerance.getLevel() < FluxKontextConstant.SAFETY_TOLERANCE_MIN ||
                    safetyTolerance.getLevel() > maxLevel) {
                throw new IllegalArgumentException(
                        String.format("安全容忍度必须在%d到%d之间",
                                FluxKontextConstant.SAFETY_TOLERANCE_MIN, maxLevel)
                );
            }
        }
    }

    /**
     * 验证编辑模式的输入图像
     */
    private void validateInputImageForEditMode() {
        if (isEditMode() && (inputImage == null || inputImage.trim().isEmpty())) {
            throw new IllegalArgumentException("编辑模式必须提供输入图像URL");
        }
    }

    /**
     * 判断是否为编辑模式
     */
    public boolean isEditMode() {
        return inputImage != null && !inputImage.trim().isEmpty();
    }

    /**
     * 判断是否为生成模式
     */
    public boolean isGenerateMode() {
        return !isEditMode();
    }

    /**
     * 构建文本生成图像请求
     */
    public static FluxKontextImageRequest generateImage(String prompt, FluxKontextAspectRatioEnum aspectRatio, String callBackUrl) {
        FluxKontextImageRequest request = new FluxKontextImageRequest();
        request.setPrompt(prompt);
        request.setAspectRatio(aspectRatio);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建图像编辑请求
     */
    public static FluxKontextImageRequest editImage(String prompt, String inputImage, FluxKontextAspectRatioEnum aspectRatio, String callBackUrl) {
        FluxKontextImageRequest request = new FluxKontextImageRequest();
        request.setPrompt(prompt);
        request.setInputImage(inputImage);
        request.setAspectRatio(aspectRatio);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建高级生成请求
     */
    public static FluxKontextImageRequest advancedGenerate(String prompt, FluxKontextAspectRatioEnum aspectRatio,
                                                           FluxKontextModelEnum model, FluxKontextSafetyToleranceEnum safetyTolerance,
                                                           String callBackUrl) {
        FluxKontextImageRequest request = new FluxKontextImageRequest();
        request.setPrompt(prompt);
        request.setAspectRatio(aspectRatio);
        request.setModel(model);
        request.setSafetyTolerance(safetyTolerance);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建高级编辑请求
     */
    public static FluxKontextImageRequest advancedEdit(String prompt, String inputImage, FluxKontextAspectRatioEnum aspectRatio,
                                                       FluxKontextModelEnum model, FluxKontextSafetyToleranceEnum safetyTolerance,
                                                       String callBackUrl) {
        FluxKontextImageRequest request = new FluxKontextImageRequest();
        request.setPrompt(prompt);
        request.setInputImage(inputImage);
        request.setAspectRatio(aspectRatio);
        request.setModel(model);
        request.setSafetyTolerance(safetyTolerance);
        request.setCallBackUrl(callBackUrl);
        return request;
    }
}