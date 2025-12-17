package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * ElevenLabs语音服务Feign客户端
 */
@FeignClient(
        name = "elevenlabs-service",
        url = "${feign.client.elevenlabs.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface ElevenLabsFeignClient {

    /**
     * 文本转语音
     */
    @PostMapping("/api/v1/jobs/createTask")
    ElevenLabsResponse textToSpeech(@Valid @RequestBody ElevenLabsTTSRequest request);

    /**
     * 语音转文本
     */
    @PostMapping("/api/v1/jobs/createTask")
    ElevenLabsResponse speechToText(@Valid @RequestBody ElevenLabsSTTRequest request);

    /**
     * 音效生成
     */
    @PostMapping("/api/v1/jobs/createTask")
    ElevenLabsResponse generateSoundEffect(@Valid @RequestBody ElevenLabsSoundEffectRequest request);

    /**
     * 音频分离
     */
    @PostMapping("/api/v1/jobs/createTask")
    ElevenLabsResponse isolateAudio(@Valid @RequestBody ElevenLabsAudioIsolationRequest request);
}