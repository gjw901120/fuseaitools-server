package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * ElevenLabs audio isolation request parameters
 */
@Data
public class ElevenlabsAudioIsolationDTO implements Serializable {

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
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validate model
        if (model != ElevenLabsModelEnum.AUDIO_ISOLATION.getCode()) {
            throw new IllegalArgumentException("Audio isolation only supports elevenlabs/audio-isolation model");
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

        // Validate file size (10MB)
        long maxSize = 10L * 1024 * 1024;
        if (audioFile.getSize() > maxSize) {
            throw new IllegalArgumentException("Audio file size cannot exceed 10MB");
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