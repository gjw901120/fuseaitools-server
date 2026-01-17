package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import com.fuse.ai.server.web.common.enums.ElevenLabsVoiceEnum;
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
import java.util.Objects;

/**
 * ElevenLabs Turbo text-to-speech request parameters
 */
@Data
public class ElevenlabsTTSDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Model name
     */
    @NotNull(message = "Model cannot be empty")
    private String model ;

    /**
     * Text to convert to speech
     */
    @NotBlank(message = "Text cannot be empty")
    @Size(max = 5000, message = "Text length cannot exceed 5000 characters")
    private String text;

    /**
     * Voice selection
     */
    private ElevenLabsVoiceEnum voice;

    /**
     * Voice stability (0-1)
     */
    @DecimalMin(value = "0.0", message = "Voice stability minimum is 0")
    @DecimalMax(value = "1.0", message = "Voice stability maximum is 1")
    private BigDecimal stability;

    /**
     * Similarity boost (0-1)
     */
    @DecimalMin(value = "0.0", message = "Similarity boost minimum is 0")
    @DecimalMax(value = "1.0", message = "Similarity boost maximum is 1")
    private BigDecimal similarityBoost;

    /**
     * Style exaggeration (0-1)
     */
    @DecimalMin(value = "0.0", message = "Style exaggeration minimum is 0")
    @DecimalMax(value = "1.0", message = "Style exaggeration maximum is 1")
    private BigDecimal style;

    /**
     * Speech speed (0.7-1.2)
     */
    @DecimalMin(value = "0.7", message = "Speech speed minimum is 0.7")
    @DecimalMax(value = "1.2", message = "Speech speed maximum is 1.2")
    private BigDecimal speed = BigDecimal.ONE;

    /**
     * Whether to return timestamps
     */
    private Boolean timestamps = false;

    /**
     * Previous text content
     */
    @Size(max = 5000, message = "Previous text length cannot exceed 5000 characters")
    private String previousText;

    /**
     * Next text content
     */
    @Size(max = 5000, message = "Next text length cannot exceed 5000 characters")
    private String nextText;

    /**
     * Language code (ISO 639-1)
     */
    @Size(max = 500, message = "Language code length cannot exceed 500 characters")
    private String languageCode;

    /**
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validate model
        if (!Objects.equals(model, "elevenlabs_text_to_speech_multilingual") && !Objects.equals(model, "elevenlabs_text_to_speech_turbo")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Turbo TTS only supports text-to-speech-turbo-2-5/text-to-speech-multilingual_v2 model");
        }

        // Validate text length
        if (text.length() > 5000) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Text length cannot exceed 5000 characters");
        }

        // Validate language code format (optional)
        if (languageCode != null && !languageCode.matches("^[a-z]{2}$")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Language code format is incorrect, should be ISO 639-1 format");
        }
    }
}