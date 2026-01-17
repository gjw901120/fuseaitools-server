package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
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
 * Suno Audio Generation Request Parameters
 */
@Data
public class SunoGenerateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Prompt describing the desired audio content
     */
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;

    /**
     * Whether to enable custom mode
     */
    @NotNull(message = "Custom mode cannot be empty")
    private Boolean customMode;

    /**
     * Whether it's instrumental only
     */
    @NotNull(message = "Instrumental flag cannot be empty")
    private Boolean instrumental;

    /**
     * Model version
     */
    @NotNull(message = "Model cannot be empty")
    private SunoModelEnum model;

    /**
     * Music style
     */
    @Size(max = 1000, message = "Style length cannot exceed 1000 characters")
    private String style;

    /**
     * Music title
     */
    @Size(max = 100, message = "Title length cannot exceed 100 characters")
    private String title;

    /**
     * Excluded music styles
     */
    private String negativeTags;

    /**
     * Vocal gender preference
     */
    private SunoVocalGenderEnum vocalGender;

    /**
     * Style adherence intensity
     */
    @DecimalMin(value = "0.0", message = "Style weight must be at least 0")
    @DecimalMax(value = "1.0", message = "Style weight must be at most 1")
    private BigDecimal styleWeight;

    /**
     * Creative deviation degree
     */
    @DecimalMin(value = "0.0", message = "Creative deviation degree must be at least 0")
    @DecimalMax(value = "1.0", message = "Creative deviation degree must be at most 1")
    private BigDecimal weirdnessConstraint;

    /**
     * Audio element weight
     */
    @DecimalMin(value = "0.0", message = "Audio weight must be at least 0")
    @DecimalMax(value = "1.0", message = "Audio weight must be at most 1")
    private BigDecimal audioWeight;

    /**
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validation in custom mode
        if (Boolean.TRUE.equals(customMode)) {
            if (style == null || style.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Style cannot be empty in custom mode");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Title cannot be empty in custom mode");
            }
            if (Boolean.FALSE.equals(instrumental) && (prompt == null || prompt.trim().isEmpty())) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Prompt cannot be empty in non-instrumental mode");
            }
        }

        // Validation in non-custom mode
        if (Boolean.FALSE.equals(customMode)) {
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Prompt cannot be empty in non-custom mode");
            }
        }

        // Validate character length based on model
        validateCharacterLimits();
    }

    /**
     * Validate character length limits based on the model
     */
    private void validateCharacterLimits() {
        if (prompt != null) {
            int promptMaxLength = getPromptMaxLength();
            if (prompt.length() > promptMaxLength) {
                throw new IllegalArgumentException("Prompt length cannot exceed " + promptMaxLength + " characters");
            }
        }

        if (style != null) {
            int styleMaxLength = getStyleMaxLength();
            if (style.length() > styleMaxLength) {
                throw new IllegalArgumentException("Style length cannot exceed " + styleMaxLength + " characters");
            }
        }

        if (title != null) {
            int titleMaxLength = getTitleMaxLength();
            if (title.length() > titleMaxLength) {
                throw new IllegalArgumentException("Title length cannot exceed " + titleMaxLength + " characters");
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