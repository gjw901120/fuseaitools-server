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
 * Suno Audio Extension Request Parameters
 */
@Data
public class SunoExtendDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Controls the source of parameters used for extension
     */
    @NotNull(message = "Parameter source flag cannot be empty")
    private Boolean defaultParamFlag;

    /**
     * Unique identifier of the audio track to be extended
     */
    @NotBlank(message = "Audio ID cannot be empty")
    private String audioId;

    /**
     * Model version
     */
    @NotNull(message = "Model cannot be empty")
    private SunoModelEnum model;

    /**
     * Extension content prompt
     */
    @Size(max = 3000, message = "Prompt length cannot exceed 3000 characters")
    private String prompt;

    /**
     * Music style
     */
    @Size(max = 200, message = "Style length cannot exceed 200 characters")
    private String style;

    /**
     * Music title
     */
    @Size(max = 80, message = "Title length cannot exceed 80 characters")
    private String title;

    /**
     * Extension start time point
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Extension start time must be greater than 0")
    private BigDecimal continueAt;

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
        // Validation in custom parameter mode
        if (Boolean.TRUE.equals(defaultParamFlag)) {
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Prompt cannot be empty in custom parameter mode");
            }
            if (style == null || style.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Style cannot be empty in custom parameter mode");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Title cannot be empty in custom parameter mode");
            }
            if (continueAt == null) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Extension start time cannot be empty in custom parameter mode");
            }
        }

        // Validate audio ID format
        if (audioId == null || audioId.trim().isEmpty()) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Audio ID cannot be empty");
        }
    }
}