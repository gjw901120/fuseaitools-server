package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ElevenLabsResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ElevenLabsManager;
import com.fuse.ai.server.manager.model.request.ElevenLabsAudioIsolationRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSTTRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsSoundEffectRequest;
import com.fuse.ai.server.manager.model.request.ElevenLabsTTSRequest;
import com.fuse.ai.server.manager.model.response.ElevenLabsResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.EnhancedAudioUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsAudioIsolationDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSTTDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsSoundEffectDTO;
import com.fuse.ai.server.web.model.dto.request.elevenlabs.ElevenlabsTTSDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ElevenlabsService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElevenlabsServiceImpl implements ElevenlabsService {

    @Autowired
    private ElevenLabsManager elevenLabsManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse elevenlabsTTS(ElevenlabsTTSDTO elevenlabsTTSDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(elevenlabsTTSDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.ELE_CHARACTER);
        extraData.setEleCharacter(elevenlabsTTSDTO.getText().getBytes(StandardCharsets.UTF_8).length);

        verifyCreditsBO verifyCreditsBO =userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        ElevenLabsTTSRequest request = new ElevenLabsTTSRequest();

        ElevenLabsTTSRequest.TTSInput input = new ElevenLabsTTSRequest.TTSInput();

        request.setModel(model.getRequestName());

        BeanUtils.copyProperties(elevenlabsTTSDTO, input);

        request.setInput(input);

        request.setCallBackUrl(callbackUrl.concat("/elevenLabs/tts"));

        ElevenLabsResponse response = elevenLabsManager.textToSpeech(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, elevenlabsTTSDTO.getText(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse elevenlabsSTT(ElevenlabsSTTDTO elevenlabsSTTDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(elevenlabsSTTDTO.getModel());

        double duration = EnhancedAudioUtil.getAudioDuration(elevenlabsSTTDTO.getAudioUrl());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.ELE_DURATION);
        extraData.setEleDuration((int) duration);

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        ElevenLabsSTTRequest request = new ElevenLabsSTTRequest();

        ElevenLabsSTTRequest.STTInput input = new ElevenLabsSTTRequest.STTInput();

        request.setModel(model.getRequestName());

        BeanUtils.copyProperties(elevenlabsSTTDTO, input);

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(elevenlabsSTTDTO.getAudioUrl());

        input.setAudioUrl(elevenlabsSTTDTO.getAudioUrl());

        request.setInput(input);

        request.setCallBackUrl(callbackUrl.concat("/elevenLabs/stt"));

        ElevenLabsResponse response = elevenLabsManager.speechToText(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, "speech to text", userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse elevenlabsAudioIsolationDTO(ElevenlabsAudioIsolationDTO elevenlabsAudioIsolationDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(elevenlabsAudioIsolationDTO.getModel());

        double duration = EnhancedAudioUtil.getAudioDuration(elevenlabsAudioIsolationDTO.getAudioUrl());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.ELE_DURATION);
        extraData.setEleDuration((int) duration);

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        ElevenLabsAudioIsolationRequest request = new ElevenLabsAudioIsolationRequest();

        ElevenLabsAudioIsolationRequest.AudioIsolationInput input = new ElevenLabsAudioIsolationRequest.AudioIsolationInput();

        request.setModel(model.getRequestName());

        BeanUtils.copyProperties(elevenlabsAudioIsolationDTO, input);

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(elevenlabsAudioIsolationDTO.getAudioUrl());

        input.setAudioUrl(elevenlabsAudioIsolationDTO.getAudioUrl());

        request.setInput(input);

        request.setCallBackUrl(callbackUrl.concat("/elevenLabs/audio-isolation"));

        ElevenLabsResponse response = elevenLabsManager.isolateAudio(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, "audio isolation", userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse elevenlabsSoundEffectDTO(ElevenlabsSoundEffectDTO elevenlabsSoundEffectDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(elevenlabsSoundEffectDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.ELE_DURATION);
        extraData.setEleDuration(elevenlabsSoundEffectDTO.getDurationSeconds().setScale(0, RoundingMode.HALF_UP).intValue());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        ElevenLabsSoundEffectRequest request = new ElevenLabsSoundEffectRequest();

        ElevenLabsSoundEffectRequest.SoundEffectInput input = new ElevenLabsSoundEffectRequest.SoundEffectInput();

        request.setModel(model.getRequestName());

        BeanUtils.copyProperties(elevenlabsSoundEffectDTO, input);

        request.setInput(input);

        request.setCallBackUrl(callbackUrl.concat("/elevenLabs/sound-effect"));

        ElevenLabsResponse response = elevenLabsManager.generateSoundEffect(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, elevenlabsSoundEffectDTO.getText(), userModelTask, verifyCreditsBO));

    }

}
