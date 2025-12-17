package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

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
    @NotNull(message = "Audio file cannot be empty")
    private MultipartFile audioFile;

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
        if (model != ElevenLabsModelEnum.SPEECH_TO_TEXT.getCode()) {
            throw new IllegalArgumentException("Speech-to-text only supports elevenlabs/speech-to-text model");
        }

        // Validate file
        if (audioFile == null || audioFile.isEmpty()) {
            throw new IllegalArgumentException("Audio file cannot be empty");
        }

        // Validate file type
        String contentType = audioFile.getContentType();
        if (contentType == null || !isSupportedAudioType(contentType)) {
            throw new IllegalArgumentException("Audio file format not supported. Supported formats: audio/mpeg, audio/wav, audio/x-wav, audio/aac, audio/mp4, audio/ogg");
        }

        // Validate file size (200MB)
        long maxSize = 200L * 1024 * 1024;
        if (audioFile.getSize() > maxSize) {
            throw new IllegalArgumentException("Audio file size cannot exceed 200MB");
        }

        // Validate language code format (optional)
        if (languageCode != null && !languageCode.matches("^[a-z]{2}$")) {
            throw new IllegalArgumentException("Language code format is incorrect, should be ISO 639-1 format");
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