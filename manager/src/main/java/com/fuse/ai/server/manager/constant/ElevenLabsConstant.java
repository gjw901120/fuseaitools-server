package com.fuse.ai.server.manager.constant;

/**
 * ElevenLabs语音相关常量
 */
public final class ElevenLabsConstant {

    private ElevenLabsConstant() {}

    // 模型类型
    public static final String MODEL_TTS_MULTILINGUAL_V2 = "elevenlabs/text-to-speech-multilingual-v2";
    public static final String MODEL_TTS_TURBO_2_5 = "elevenlabs/text-to-speech-turbo-2-5";
    public static final String MODEL_SPEECH_TO_TEXT = "elevenlabs/speech-to-text";
    public static final String MODEL_SOUND_EFFECT_V2 = "elevenlabs/sound-effect-v2";
    public static final String MODEL_AUDIO_ISOLATION = "elevenlabs/audio-isolation";

    // 文本长度限制
    public static final int TEXT_MAX_LENGTH = 5000;
    public static final int LANGUAGE_CODE_MAX_LENGTH = 500;

    // 音频文件大小限制
    public static final long AUDIO_MAX_SIZE_SPEECH_TO_TEXT = 200 * 1024 * 1024; // 200MB
    public static final long AUDIO_MAX_SIZE_AUDIO_ISOLATION = 10 * 1024 * 1024; // 10MB

    // 数值范围
    public static final double MIN_STABILITY = 0.0;
    public static final double MAX_STABILITY = 1.0;
    public static final double MIN_SIMILARITY_BOOST = 0.0;
    public static final double MAX_SIMILARITY_BOOST = 1.0;
    public static final double MIN_STYLE = 0.0;
    public static final double MAX_STYLE = 1.0;
    public static final double MIN_SPEED = 0.7;
    public static final double MAX_SPEED = 1.2;
    public static final double MIN_DURATION = 0.5;
    public static final double MAX_DURATION = 22.0;
    public static final double MIN_PROMPT_INFLUENCE = 0.0;
    public static final double MAX_PROMPT_INFLUENCE = 1.0;

    // 步长
    public static final double STEP_SMALL = 0.01;
    public static final double STEP_DURATION = 0.1;

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INSUFFICIENT_CREDITS = 402;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_RATE_LIMIT = 429;
    public static final int CODE_SERVICE_UNAVAILABLE = 455;
    public static final int CODE_SERVER_ERROR = 500;
}