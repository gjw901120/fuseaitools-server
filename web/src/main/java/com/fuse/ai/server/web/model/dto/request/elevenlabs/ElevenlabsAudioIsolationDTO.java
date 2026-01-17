package com.fuse.ai.server.web.model.dto.request.elevenlabs;

import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

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
    @NotNull(message = "Audio url cannot be empty")
    private String audioUrl;

    /**
     * Business parameter validation
     */
    public void validateBusinessRules() {
        // Validate model
        if (!Objects.equals(model, "elevenlabs_audio_isolation")) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Audio isolation only supports audio-isolation model");
        }

        // Validate file
        if (audioUrl == null || audioUrl.isEmpty()) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR,"Audio url cannot be empty");
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