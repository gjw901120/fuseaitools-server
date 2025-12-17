package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ElevenLabsResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ElevenLabsManager;
import com.fuse.ai.server.manager.model.request.ElevenLabsAudioIsolationRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSTTRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSoundEffectRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsTTSRequest;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsAudioIsolationDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSTTDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSoundEffectDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsTTSDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ElevenlabsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ElevenlabsServiceImpl implements ElevenlabsService {

    @Autowired
    private ElevenLabsManager elevenLabsManager;

    @Autowired
    private RecordsService recordsService;

    @Override
    public BaseResponse elevenlabsTTS(ElevenlabsTTSDTO elevenlabsTTSDTO) {

        // 实现视频生成逻辑
        ElevenLabsTTSRequest request = new ElevenLabsTTSRequest();

        ElevenLabsTTSRequest.TTSInput input = new ElevenLabsTTSRequest.TTSInput();

        request.setModel(elevenlabsTTSDTO.getModel());

        BeanUtils.copyProperties(elevenlabsTTSDTO, input);

        request.setInput(input);

        ElevenLabsResponse response = elevenLabsManager.textToSpeech(request);

        if(!ElevenLabsResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                new ArrayList<>(),
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(elevenlabsTTSDTO.getModel(), userModelTask));

    }

    @Override
    public BaseResponse elevenlabsSTT(ElevenlabsSTTDTO elevenlabsSTTDTO) {

        // 实现视频生成逻辑
        ElevenLabsSTTRequest request = new ElevenLabsSTTRequest();

        ElevenLabsSTTRequest.STTInput input = new ElevenLabsSTTRequest.STTInput();

        request.setModel(elevenlabsSTTDTO.getModel());

        BeanUtils.copyProperties(elevenlabsSTTDTO, input);

        List<String> inputUrls = new ArrayList<>();

        String audioUrl = "";

        inputUrls.add(audioUrl);

        input.setAudioUrl(audioUrl);

        request.setInput(input);

        ElevenLabsResponse response = elevenLabsManager.speechToText(request);

        if(!ElevenLabsResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                inputUrls,
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("elevenlabs_speech_to_text", userModelTask));

    }

    @Override
    public BaseResponse elevenlabsAudioIsolationDTO(ElevenlabsAudioIsolationDTO elevenlabsAudioIsolationDTO) {

        // 实现视频生成逻辑
        ElevenLabsAudioIsolationRequest request = new ElevenLabsAudioIsolationRequest();

        ElevenLabsAudioIsolationRequest.AudioIsolationInput input = new ElevenLabsAudioIsolationRequest.AudioIsolationInput();

        request.setModel(elevenlabsAudioIsolationDTO.getModel());

        BeanUtils.copyProperties(elevenlabsAudioIsolationDTO, input);

        List<String> inputUrls = new ArrayList<>();

        String audioUrl = "";

        inputUrls.add(audioUrl);

        input.setAudioUrl(audioUrl);

        request.setInput(input);

        ElevenLabsResponse response = elevenLabsManager.isolateAudio(request);

        if(!ElevenLabsResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                inputUrls,
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("elevenlabs_audio_isolation", userModelTask));

    }

    @Override
    public BaseResponse elevenlabsSoundEffectDTO(ElevenlabsSoundEffectDTO elevenlabsSoundEffectDTO) {

        // 实现视频生成逻辑
        ElevenLabsSoundEffectRequest request = new ElevenLabsSoundEffectRequest();

        ElevenLabsSoundEffectRequest.SoundEffectInput input = new ElevenLabsSoundEffectRequest.SoundEffectInput();

        request.setModel(elevenlabsSoundEffectDTO.getModel());

        BeanUtils.copyProperties(elevenlabsSoundEffectDTO, input);

        request.setInput(input);

        ElevenLabsResponse response = elevenLabsManager.generateSoundEffect(request);

        if(!ElevenLabsResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                new ArrayList<>(),
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("elevenlabs_sound_effect", userModelTask));

    }

}
