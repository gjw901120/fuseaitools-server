package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.callback.elevenlabs.ElevenLabsCallbackRequest;
import com.fuse.ai.server.web.service.ElevenLabsCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ElevenLabs回调控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/callback/elevenlabs")
@RequiredArgsConstructor
public class ElevenLabsCallbackController {

    private final ElevenLabsCallbackService elevenLabsCallbackService;

    /**
     * 处理ElevenLabs回调
     */
    @PostMapping
    public String Callback(@Valid @RequestBody ElevenLabsCallbackRequest request) {
        try {

            log.info("Received ElevenLabs callback: request={}", request);

            elevenLabsCallbackService.processCallback(request);

            return "success";

        } catch (Exception e) {
            String taskId = request.getData() != null ? request.getData().getTaskId() : "unknown";
            log.error("ElevenLabs callback processing failed: taskId={}, error={}",
                    taskId, e.getMessage(), e);


            return "failed";
        }
    }

    /**
     * 处理文本转语音回调（专用接口）
     */
    @PostMapping("/tts")
    public String TtsCallback(@Valid @RequestBody ElevenLabsCallbackRequest request) {
        try {
            log.info("Received ElevenLabs TTS callback: request={}", request);

            elevenLabsCallbackService.processTextToSpeechCallback(request);


            return "success";

        } catch (Exception e) {
            log.error("ElevenLabs TTS callback processing failed: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    /**
     * 处理语音转文本回调（专用接口）
     */
    @PostMapping("/stt")
    public String SttCallback(@Valid @RequestBody ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received ElevenLabs STT callback: request={}", request);

            elevenLabsCallbackService.processSpeechToTextCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("ElevenLabs STT callback processing failed: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    /**
     * 处理音频分离回调（专用接口）
     */
    @PostMapping("/audio-isolation")
    public String AudioIsolationCallback(@Valid @RequestBody ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received ElevenLabs audio isolation callback: request={}", request);

            elevenLabsCallbackService.processAudioIsolationCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("ElevenLabs audio isolation callback processing failed: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    /**
     * 处理音效生成回调（专用接口）
     */
    @PostMapping("/sound-effect")
    public String SoundEffectCallback(@Valid @RequestBody ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received ElevenLabs sound effect callback: request={}", request);

            elevenLabsCallbackService.processSoundEffectCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("ElevenLabs sound effect callback processing failed: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

}