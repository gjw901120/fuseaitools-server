package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno音频生成请求参数
 */
@Data
public class SunoGenerateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需音频内容的提示词
     */
    @NotBlank(message = "提示词不能为空")
    private String prompt;

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
     * 模型版本
     */
    @NotNull(message = "模型不能为空")
    private SunoModelEnum model;

    /**
     * 音乐风格
     */
    @Size(max = 1000, message = "风格长度不能超过1000个字符")
    private String style;

    /**
     * 音乐标题
     */
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    /**
     * 排除的音乐风格
     */
    private String negativeTags;

    /**
     * 人声性别偏好
     */
    private SunoVocalGenderEnum vocalGender;

    /**
     * 风格遵循强度
     */
    @DecimalMin(value = "0.0", message = "风格权重最小为0")
    @DecimalMax(value = "1.0", message = "风格权重最大为1")
    private BigDecimal styleWeight;

    /**
     * 创意偏离程度
     */
    @DecimalMin(value = "0.0", message = "创意偏离程度最小为0")
    @DecimalMax(value = "1.0", message = "创意偏离程度最大为1")
    private BigDecimal weirdnessConstraint;

    /**
     * 音频要素权重
     */
    @DecimalMin(value = "0.0", message = "音频权重最小为0")
    @DecimalMax(value = "1.0", message = "音频权重最大为1")
    private BigDecimal audioWeight;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 自定义模式下的校验
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

        // 非自定义模式下的校验
        if (Boolean.FALSE.equals(customMode)) {
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("非自定义模式下提示词不能为空");
            }
        }

        // 根据模型校验字符长度
        validateCharacterLimits();
    }

    /**
     * 根据模型校验字符长度限制
     */
    private void validateCharacterLimits() {
        if (prompt != null) {
            int promptMaxLength = getPromptMaxLength();
            if (prompt.length() > promptMaxLength) {
                throw new IllegalArgumentException("提示词长度不能超过" + promptMaxLength + "个字符");
            }
        }

        if (style != null) {
            int styleMaxLength = getStyleMaxLength();
            if (style.length() > styleMaxLength) {
                throw new IllegalArgumentException("风格长度不能超过" + styleMaxLength + "个字符");
            }
        }

        if (title != null) {
            int titleMaxLength = getTitleMaxLength();
            if (title.length() > titleMaxLength) {
                throw new IllegalArgumentException("标题长度不能超过" + titleMaxLength + "个字符");
            }
        }
    }

    private int getPromptMaxLength() {
        if (model == null) return 3000;
        return switch (model) {
            case V3_5, V4 -> 3000;
            case V4_5, V4_5PLUS, V5 -> 5000;
            default -> 3000;
        };
    }

    private int getStyleMaxLength() {
        if (model == null) return 200;
        return switch (model) {
            case V3_5, V4 -> 200;
            case V4_5, V4_5PLUS, V5 -> 1000;
            default -> 200;
        };
    }

    private int getTitleMaxLength() {
        if (model == null) return 80;
        return switch (model) {
            case V3_5, V4 -> 80;
            case V4_5, V4_5PLUS, V5 -> 100;
            default -> 80;
        };
    }
}