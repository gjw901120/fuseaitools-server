package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno Upload Extension Request Parameters
 */
@Data
public class SunoUploadExtendDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Uploaded audio file
     */
    @NotNull(message = "File url cannot be empty")
    private String fileUrl;

    /**
     * Whether to enable custom parameter mode
     */
    @NotNull(message = "Custom parameter mode cannot be empty")
    private Boolean defaultParamFlag;

    /**
     * Model version
     */
    @NotNull(message = "Model cannot be empty")
    private SunoModelEnum model;

    /**
     * Whether it's instrumental only
     */
    private Boolean instrumental;

    /**
     * Extension content prompt
     */
    @Size(max = 5000, message = "Prompt length cannot exceed 5000 characters")
    private String prompt;

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
        // Validate file
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "File url cannot be empty");
        }

        // Validation in custom parameter mode
        if (Boolean.TRUE.equals(defaultParamFlag)) {
            if (style == null || style.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Style cannot be empty in custom parameter mode");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Title cannot be empty in custom parameter mode");
            }
            if (continueAt == null) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Extension start time cannot be empty in custom parameter mode");
            }
            if (Boolean.FALSE.equals(instrumental) && (prompt == null || prompt.trim().isEmpty())) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Prompt cannot be empty in non-instrumental mode");
            }
        }
    }
}