package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsOutputFormatEnum;
import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * ElevenLabs sound effect generation request parameters
 */
@Data
public class ElevenlabsSoundEffectDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Model name
     */
    @NotNull(message = "Model cannot be empty")
    private String model;

    /**
     * Text describing the sound effect to generate
     */
    @NotBlank(message = "Sound effect description text cannot be empty")
    @Size(max = 5000, message = "Sound effect description text length cannot exceed 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String text;

    /**
     * Whether to loop
     */
    private Boolean loop = false;

    /**
     * Duration in seconds
     */
    @DecimalMin(value = "0.5", message = "Duration minimum is 0.5 seconds")
    @DecimalMax(value = "22.0", message = "Duration maximum is 22 seconds")
    private BigDecimal durationSeconds;

    /**
     * Prompt influence (0-1)
     */
    @DecimalMin(value = "0.0", message = "Prompt influence minimum is 0")
    @DecimalMax(value = "1.0", message = "Prompt influence maximum is 1")
    private BigDecimal promptInfluence = BigDecimal.valueOf(0.3);

    /**
     * Output format
     */
    private ElevenLabsOutputFormatEnum outputFormat = ElevenLabsOutputFormatEnum.MP3_44100_128;

    /**
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validate model
        if (!Objects.equals(model, "elevenlabs_sound_effect")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Sound effect generation only supports sound-effect-v2 model");
        }

        // Validate text length
        if (text.length() > 5000) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Sound effect description text length cannot exceed 5000 characters");
        }

        // Validate duration range
        if (durationSeconds != null &&
                (durationSeconds.compareTo(BigDecimal.valueOf(0.5)) < 0 ||
                        durationSeconds.compareTo(BigDecimal.valueOf(22.0)) > 0)) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Duration must be between 0.5 and 22 seconds");
        }
    }
}