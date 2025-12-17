package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.callback.elevenlabs.ElevenLabsCallbackRequest;

/**
 * ElevenLabs回调服务
 */
public interface ElevenLabsCallbackService {

    /**
     * 处理ElevenLabs回调
     */
    void processCallback(ElevenLabsCallbackRequest request);

    /**
     * 处理文本转语音回调
     */
    void processTextToSpeechCallback(ElevenLabsCallbackRequest request);

    /**
     * 处理语音转文本回调
     */
    void processSpeechToTextCallback(ElevenLabsCallbackRequest request);

    /**
     * 处理音频分离回调
     */
    void processAudioIsolationCallback(ElevenLabsCallbackRequest request);

    /**
     * 处理音效生成回调
     */
    void processSoundEffectCallback(ElevenLabsCallbackRequest request);
}