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
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno Add Vocals Request Parameters
 */
@Data
public class SunoAddVocalsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Prompt for generating audio
     */
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;

    /**
     * Music title
     */
    @NotBlank(message = "Title cannot be empty")
    private String title;

    /**
     * Excluded music styles
     */
    @NotBlank(message = "Excluded styles cannot be empty")
    private String negativeTags;

    /**
     * Music style
     */
    @NotBlank(message = "Style cannot be empty")
    private String style;

    /**
     * Uploaded audio file
     */
    @NotNull(message = "File url cannot be empty")
    private String fileUrl;

    /**
     * Model version
     */
    @NotNull(message = "Model cannot be empty")
    private SunoModelEnum model = SunoModelEnum.V4_5PLUS;

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

        // Validate model restrictions
        if (model != SunoModelEnum.V4_5PLUS && model != SunoModelEnum.V5) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "Adding vocals only supports V4_5PLUS and V5 models");
        }
    }
}