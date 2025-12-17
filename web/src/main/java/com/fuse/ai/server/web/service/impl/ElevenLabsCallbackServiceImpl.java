package com.fuse.ai.server.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.web.model.dto.request.callback.elevenlabs.*;
import com.fuse.ai.server.web.common.enums.ElevenLabsModelEnum;
import com.fuse.ai.server.web.service.ElevenLabsCallbackService;
import com.fuse.ai.server.web.service.RecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ElevenLabs回调服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElevenLabsCallbackServiceImpl implements ElevenLabsCallbackService {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private RecordsService recordsService;

    @Override
    public void processCallback(ElevenLabsCallbackRequest request) {
        try {

            // 根据模型类型分发到不同的处理方法
            String model = request.getData().getModel();
            ElevenLabsModelEnum modelEnum = ElevenLabsModelEnum.getByCode(model);

            if (modelEnum == null) {
                log.warn("Unknown ElevenLabs model: {}", model);
                return;
            }

            switch (modelEnum) {
                case TEXT_TO_SPEECH_TURBO, TEXT_TO_SPEECH_MULTILINGUAL -> processTextToSpeechCallback(request);
                case SPEECH_TO_TEXT -> processSpeechToTextCallback(request);
                case AUDIO_ISOLATION -> processAudioIsolationCallback(request);
                case SOUND_EFFECT -> processSoundEffectCallback(request);
                default -> log.warn("Unsupported ElevenLabs model: {}", model);
            }

            log.info("ElevenLabs callback processed successfully: taskId={}",
                    request.getData().getTaskId());

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process ElevenLabs callback: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);
            throw new RuntimeException("ElevenLabs callback processing failed", e);
        }
    }

    @Override
    public void processTextToSpeechCallback(ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            log.info("Processing TTS callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析结果URL
                ElevenLabsTtsParam.TtsResult result = objectMapper.readValue(resultJson, ElevenLabsTtsParam.TtsResult.class);
                log.info("TTS task completed: taskId={}, resultUrls={}",
                        taskId, result.getResultUrls());

                // 更新任务状态为成功
                List<String> outputUrl = new ArrayList<>();
                recordsService.completed(request.getData().getTaskId(), outputUrl, request);

            } else if ("fail".equals(state)) {
                // 处理失败情况
                log.error("TTS task failed: taskId={}, failMsg={}",
                        taskId, request.getData().getFailMsg());

                recordsService.failed(request.getData().getTaskId(), request);
            }

            // 记录积分消耗


        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process TTS callback: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);
            throw new RuntimeException("TTS callback processing failed", e);
        }
    }

    @Override
    public void processSpeechToTextCallback(ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            log.info("Processing STT callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                // 解析语音转文本结果
                ElevenLabsSttParam.SttResult result = objectMapper.readValue(resultJson, ElevenLabsSttParam.SttResult.class);
                ElevenLabsSttParam.SttResult.SttResultObject resultObject = result.getResultObject();

                log.info("STT task completed: taskId={}, text={}, languageCode={}",
                        taskId, resultObject.getText(), resultObject.getLanguageCode());

                // 如果有单词信息，记录详细信息
                if (resultObject.getWords() != null && resultObject.getWords().length > 0) {
                    log.debug("STT task word count: {}", resultObject.getWords().length);
                }

                // 更新任务状态为成功
                List<String> outputUrl = new ArrayList<>();
                recordsService.completed(request.getData().getTaskId(), outputUrl, request);

            } else if ("fail".equals(state)) {
                log.error("STT task failed: taskId={}, failMsg={}",
                        taskId, request.getData().getFailMsg());

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process STT callback: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);
            throw new RuntimeException("STT callback processing failed", e);
        }
    }

    @Override
    public void processAudioIsolationCallback(ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            log.info("Processing audio isolation callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                ElevenLabsAudioIsolationParam.AudioIsolationResult result = objectMapper.readValue(resultJson, ElevenLabsAudioIsolationParam.AudioIsolationResult.class);
                log.info("Audio isolation task completed: taskId={}, resultUrls={}",
                        taskId, result.getResultUrls());

                List<String> outputUrl = new ArrayList<>();
                recordsService.completed(request.getData().getTaskId(), outputUrl, request);

            } else if ("fail".equals(state)) {
                log.error("Audio isolation task failed: taskId={}, failMsg={}",
                        taskId, request.getData().getFailMsg());

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process audio isolation callback: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);
            throw new RuntimeException("Audio isolation callback processing failed", e);
        }
    }

    @Override
    public void processSoundEffectCallback(ElevenLabsCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            String state = request.getData().getState();
            String resultJson = request.getData().getResultJson();

            log.info("Processing sound effect callback: taskId={}, state={}", taskId, state);

            if ("success".equals(state)) {
                ElevenLabsSoundEffectParam.SoundEffectResult result = objectMapper.readValue(resultJson, ElevenLabsSoundEffectParam.SoundEffectResult.class);
                log.info("Sound effect task completed: taskId={}, resultUrls={}",
                        taskId, result.getResultUrls());
                List<String> outputUrl = new ArrayList<>();
                recordsService.completed(request.getData().getTaskId(), outputUrl, request);

            } else if ("fail".equals(state)) {
                log.error("Sound effect task failed: taskId={}, failMsg={}",
                        taskId, request.getData().getFailMsg());

                recordsService.failed(request.getData().getTaskId(), request);
            }

        } catch (Exception e) {
            recordsService.failed(request.getData().getTaskId(), request);
            log.error("Failed to process sound effect callback: taskId={}, error={}",
                    request.getData().getTaskId(), e.getMessage(), e);
            throw new RuntimeException("Sound effect callback processing failed", e);
        }
    }

}