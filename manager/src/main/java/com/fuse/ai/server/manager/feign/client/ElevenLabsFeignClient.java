package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * ElevenLabs语音服务Feign客户端
 */
@FeignClient(
        name = "elevenlabs-service",
        url = "${feign.api.elevenlabs.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface ElevenLabsFeignClient {

    /**
     * 文本转语音
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ElevenLabsResponse textToSpeech(@Valid @RequestBody ElevenLabsTTSRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 语音转文本
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ElevenLabsResponse speechToText(@Valid @RequestBody ElevenLabsSTTRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 音效生成
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ElevenLabsResponse generateSoundEffect(@Valid @RequestBody ElevenLabsSoundEffectRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 音频分离
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ElevenLabsResponse isolateAudio(@Valid @RequestBody ElevenLabsAudioIsolationRequest request, @RequestParam("apiKey") String apiKey);
}