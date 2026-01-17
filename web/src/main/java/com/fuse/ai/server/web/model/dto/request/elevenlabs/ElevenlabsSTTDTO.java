package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * ElevenLabs speech-to-text request parameters
 */
@Data
public class ElevenlabsSTTDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Model name
     */
    @NotNull(message = "Model cannot be empty")
    private String model;

    /**
     * Audio file
     */
    @NotNull(message = "Audio url cannot be empty")
    private String audioUrl;

    /**
     * Language code
     */
    @Size(max = 500, message = "Language code length cannot exceed 500 characters")
    private String languageCode;

    /**
     * Whether to tag audio events
     */
    private Boolean tagAudioEvents = false;

    /**
     * Whether to annotate speaker
     */
    private Boolean diarize = false;

    /**
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validate model
        if (!Objects.equals(model, "elevenlabs_speech_to_text")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Speech-to-text only supports speech-to-text model");
        }

        // Validate file
        if (audioUrl == null || audioUrl.isEmpty()) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Audio url cannot be empty");
        }

        // Validate language code format (optional)
        if (languageCode != null && !languageCode.matches("^[a-z]{2}$")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Language code format is incorrect, should be ISO 639-1 format");
        }
    }

    private boolean isSupportedAudioType(String contentType) {
        return contentType.startsWith("audio/mpeg") ||
                contentType.startsWith("audio/wav") ||
                contentType.startsWith("audio/x-wav") ||
                contentType.startsWith("audio/aac") ||
                contentType.startsWith("audio/mp4") ||
                contentType.startsWith("audio/ogg");
    }
}