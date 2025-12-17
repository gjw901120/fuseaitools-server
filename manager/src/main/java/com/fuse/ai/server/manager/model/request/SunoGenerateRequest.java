package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.SunoConstant;
import com.fuse.ai.server.manager.enums.SunoModelEnum;
import com.fuse.ai.server.manager.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 生成音乐请求参数
 */
@Data
public class SunoGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需音频内容的提示词
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = SunoConstant.PROMPT_MAX_LENGTH_V4_5_PLUS_V5, message = "提示词长度不能超过" + SunoConstant.PROMPT_MAX_LENGTH_V4_5_PLUS_V5 + "个字符")
    private String prompt;

    /**
     * 生成音频的音乐风格规范
     */
    @Size(max = SunoConstant.STYLE_MAX_LENGTH_V4_5_PLUS_V5, message = "风格描述长度不能超过" + SunoConstant.STYLE_MAX_LENGTH_V4_5_PLUS_V5 + "个字符")
    private String style;

    /**
     * 生成音乐曲目的标题
     */
    @Size(max = SunoConstant.TITLE_MAX_LENGTH_V4_5_PLUS_V5, message = "标题长度不能超过" + SunoConstant.TITLE_MAX_LENGTH_V4_5_PLUS_V5 + "个字符")
    private String title;

    /**
     * 是否启用自定义模式
     */
    @NotNull(message = "自定义模式不能为空")
    private Boolean customMode;

    /**
     * 是否为纯音乐
     */
    @NotNull(message = "是否为纯音乐不能为空")
    private Boolean instrumental;

    /**
     * 用于生成的AI模型版本
     */
    @NotNull(message = "模型版本不能为空")
    private SunoModelEnum model;

    /**
     * 回调URL
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 排除的音乐风格或特征
     */
    private String negativeTags;

    /**
     * 人声性别偏好
     */
    private SunoVocalGenderEnum vocalGender;

    /**
     * 风格遵循强度
     */
    @DecimalMin(value = "0.0", message = "风格权重不能小于0")
    @DecimalMax(value = "1.0", message = "风格权重不能大于1")
    @Digits(integer = 1, fraction = 2, message = "风格权重最多保留两位小数")
    private Double styleWeight;

    /**
     * 实验性偏离程度控制
     */
    @DecimalMin(value = "0.0", message = "创意偏离度不能小于0")
    @DecimalMax(value = "1.0", message = "创意偏离度不能大于1")
    @Digits(integer = 1, fraction = 2, message = "创意偏离度最多保留两位小数")
    private Double weirdnessConstraint;

    /**
     * 音频要素相对权重
     */
    @DecimalMin(value = "0.0", message = "音频权重不能小于0")
    @DecimalMax(value = "1.0", message = "音频权重不能大于1")
    @Digits(integer = 1, fraction = 2, message = "音频权重最多保留两位小数")
    private Double audioWeight;

    /**
     * 构建基础生成请求
     */
    public static SunoGenerateRequest build(String prompt, Boolean customMode, Boolean instrumental,
                                            SunoModelEnum model, String callBackUrl) {
        SunoGenerateRequest request = new SunoGenerateRequest();
        request.setPrompt(prompt);
        request.setCustomMode(customMode);
        request.setInstrumental(instrumental);
        request.setModel(model);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建完整生成请求
     */
    public static SunoGenerateRequest buildFull(String prompt, String style, String title,
                                                Boolean customMode, Boolean instrumental,
                                                SunoModelEnum model, String callBackUrl) {
        SunoGenerateRequest request = build(prompt, customMode, instrumental, model, callBackUrl);
        request.setStyle(style);
        request.setTitle(title);
        return request;
    }

    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        // 自定义模式下的验证
        if (Boolean.TRUE.equals(customMode)) {
            if (style == null || style.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义模式下风格不能为空");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义模式下标题不能为空");
            }
            if (Boolean.FALSE.equals(instrumental) && (prompt == null || prompt.trim().isEmpty())) {
                throw new IllegalArgumentException("非纯音乐模式下提示词不能为空");
            }
        }

        // 模型特定的字符限制验证
        validateModelSpecificLimits();
    }

    /**
     * 验证模型特定的限制
     */
    private void validateModelSpecificLimits() {
        if (model != null) {
            int promptMaxLength;
            int styleMaxLength;
            int titleMaxLength;

            switch (model) {
                case V3_5:
                case V4:
                    promptMaxLength = SunoConstant.PROMPT_MAX_LENGTH_V3_5_V4;
                    styleMaxLength = SunoConstant.STYLE_MAX_LENGTH_V3_5_V4;
                    titleMaxLength = SunoConstant.TITLE_MAX_LENGTH_V3_5_V4;
                    break;
                case V4_5:
                case V4_5PLUS:
                case V5:
                default:
                    promptMaxLength = SunoConstant.PROMPT_MAX_LENGTH_V4_5_PLUS_V5;
                    styleMaxLength = SunoConstant.STYLE_MAX_LENGTH_V4_5_PLUS_V5;
                    titleMaxLength = SunoConstant.TITLE_MAX_LENGTH_V4_5_PLUS_V5;
                    break;
            }

            if (prompt != null && prompt.length() > promptMaxLength) {
                throw new IllegalArgumentException(model.getDescription() + "提示词长度不能超过" + promptMaxLength + "字符");
            }
            if (style != null && style.length() > styleMaxLength) {
                throw new IllegalArgumentException(model.getDescription() + "风格描述长度不能超过" + styleMaxLength + "字符");
            }
            if (title != null && title.length() > titleMaxLength) {
                throw new IllegalArgumentException(model.getDescription() + "标题长度不能超过" + titleMaxLength + "字符");
            }
        }
    }
}