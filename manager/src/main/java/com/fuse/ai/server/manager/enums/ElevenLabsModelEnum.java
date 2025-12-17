package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum ElevenLabsModelEnum {
    TTS_MULTILINGUAL_V2("elevenlabs/text-to-speech-multilingual-v2", "多语言文本转语音V2"),
    TTS_TURBO_2_5("elevenlabs/text-to-speech-turbo-2-5", "Turbo文本转语音2.5"),
    SPEECH_TO_TEXT("elevenlabs/speech-to-text", "语音转文本"),
    SOUND_EFFECT_V2("elevenlabs/sound-effect-v2", "音效生成V2"),
    AUDIO_ISOLATION("elevenlabs/audio-isolation", "音频分离");

    private final String code;
    private final String description;

    ElevenLabsModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ElevenLabsModelEnum getByCode(String code) {
        for (ElevenLabsModelEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}