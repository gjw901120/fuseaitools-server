package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * ElevenLabs模型枚举
 */
@Getter
public enum ElevenLabsModelEnum {

    TEXT_TO_SPEECH_TURBO("elevenlabs/text-to-speech-turbo-2-5", "文本转语音Turbo模型"),
    TEXT_TO_SPEECH_MULTILINGUAL("elevenlabs/text-to-speech-multilingual-v2", "多语言文本转语音模型"),
    SPEECH_TO_TEXT("elevenlabs/speech-to-text", "语音转文本模型"),
    AUDIO_ISOLATION("elevenlabs/audio-isolation", "音频分离模型"),
    SOUND_EFFECT("elevenlabs/sound-effect-v2", "音效生成模型");

    private final String code;
    private final String description;

    ElevenLabsModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ElevenLabsModelEnum getByCode(String code) {
        for (ElevenLabsModelEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return null;
    }
}