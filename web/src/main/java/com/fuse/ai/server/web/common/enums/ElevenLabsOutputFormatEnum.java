package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * ElevenLabs输出格式枚举
 */
@Getter
public enum ElevenLabsOutputFormatEnum {

    MP3_22050_32("mp3_22050_32", "MP3 22050Hz 32kbps"),
    MP3_44100_32("mp3_44100_32", "MP3 44100Hz 32kbps"),
    MP3_44100_64("mp3_44100_64", "MP3 44100Hz 64kbps"),
    MP3_44100_96("mp3_44100_96", "MP3 44100Hz 96kbps"),
    MP3_44100_128("mp3_44100_128", "MP3 44100Hz 128kbps"),
    MP3_44100_192("mp3_44100_192", "MP3 44100Hz 192kbps"),
    PCM_8000("pcm_8000", "PCM 8000Hz"),
    PCM_16000("pcm_16000", "PCM 16000Hz"),
    PCM_22050("pcm_22050", "PCM 22050Hz"),
    PCM_24000("pcm_24000", "PCM 24000Hz"),
    PCM_44100("pcm_44100", "PCM 44100Hz"),
    PCM_48000("pcm_48000", "PCM 48000Hz"),
    ULAW_8000("ulaw_8000", "μ-law 8000Hz"),
    ALAW_8000("alaw_8000", "A-law 8000Hz"),
    OPUS_48000_32("opus_48000_32", "Opus 48000Hz 32kbps"),
    OPUS_48000_64("opus_48000_64", "Opus 48000Hz 64kbps"),
    OPUS_48000_96("opus_48000_96", "Opus 48000Hz 96kbps"),
    OPUS_48000_128("opus_48000_128", "Opus 48000Hz 128kbps"),
    OPUS_48000_192("opus_48000_192", "Opus 48000Hz 192kbps");

    private final String code;
    private final String description;

    ElevenLabsOutputFormatEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ElevenLabsOutputFormatEnum getByCode(String code) {
        for (ElevenLabsOutputFormatEnum format : values()) {
            if (format.getCode().equals(code)) {
                return format;
            }
        }
        return MP3_44100_128;
    }
}