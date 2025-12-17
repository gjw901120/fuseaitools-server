package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.ElevenLabsAudioIsolationRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSTTRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSoundEffectRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsTTSRequest;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;

public interface ElevenLabsManager {

    /**
     * 文本转语音
     */
    ElevenLabsResponse textToSpeech(ElevenLabsTTSRequest request);

    /**
     * 语音转文本
     */
    ElevenLabsResponse speechToText(ElevenLabsSTTRequest request);

    /**
     * 音效生成
     */
    ElevenLabsResponse generateSoundEffect(ElevenLabsSoundEffectRequest request);

    /**
     * 音频分离
     */
    ElevenLabsResponse isolateAudio(ElevenLabsAudioIsolationRequest request);
}
