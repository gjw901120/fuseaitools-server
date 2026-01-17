package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.ElevenLabsFeignClient;
import com.fuse.ai.server.manager.manager.ElevenLabsManager;
import com.fuse.ai.server.manager.model.request.ElevenLabsAudioIsolationRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSTTRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSoundEffectRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsTTSRequest;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElevenLabsManagerImpl implements ElevenLabsManager {

    @Autowired
    private ElevenLabsFeignClient elevenLabsFeignClient;

    /**
     * 文本转语音
     */
    @Override
    public ElevenLabsResponse textToSpeech(ElevenLabsTTSRequest request, String apiKey) {
        return elevenLabsFeignClient.textToSpeech(request, apiKey);
    }

    /**
     * 语音转文本
     */
    @Override
    public ElevenLabsResponse speechToText(ElevenLabsSTTRequest request, String apiKey) {
        return elevenLabsFeignClient.speechToText(request, apiKey);
    }

    /**
     * 音效生成
     */
    @Override
    public ElevenLabsResponse generateSoundEffect(ElevenLabsSoundEffectRequest request, String apiKey) {
        return elevenLabsFeignClient.generateSoundEffect(request, apiKey);
    }

    /**
     * 音频分离
     */
    @Override
    public ElevenLabsResponse isolateAudio(ElevenLabsAudioIsolationRequest request, String apiKey) {
        return elevenLabsFeignClient.isolateAudio(request, apiKey);
    }
}
