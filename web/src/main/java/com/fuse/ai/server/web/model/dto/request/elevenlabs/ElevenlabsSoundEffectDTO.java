package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import com.fuse.ai.server.web.common.enums.ElevenLabsOutputFormatEnum;
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
        if (model != ElevenLabsModelEnum.SOUND_EFFECT.getCode()) {
            throw new IllegalArgumentException("Sound effect generation only supports elevenlabs/sound-effect-v2 model");
        }

        // Validate text length
        if (text.length() > 5000) {
            throw new IllegalArgumentException("Sound effect description text length cannot exceed 5000 characters");
        }

        // Validate duration range
        if (durationSeconds != null &&
                (durationSeconds.compareTo(BigDecimal.valueOf(0.5)) < 0 ||
                        durationSeconds.compareTo(BigDecimal.valueOf(22.0)) > 0)) {
            throw new IllegalArgumentException("Duration must be between 0.5 and 22 seconds");
        }
    }
}