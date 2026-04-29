package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.HappyHorseManager;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ReferenceToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1TextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1VideoEditRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ReferenceToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1TextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1VideoEditDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.HappyHorseService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class HappyHorseServiceImpl implements HappyHorseService {

    @Autowired
    private HappyHorseManager happyHorseManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse v1TextToVideo(HappyHorseV1TextToVideoDTO happyHorseV1TextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(happyHorseV1TextToVideoDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(happyHorseV1TextToVideoDTO.getDuration()));
        extraData.setQuality(happyHorseV1TextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HappyHorseV1TextToVideoRequest request = new HappyHorseV1TextToVideoRequest();

        HappyHorseV1TextToVideoRequest.TextToVideoInput input = new HappyHorseV1TextToVideoRequest.TextToVideoInput();

        input.setPrompt(happyHorseV1TextToVideoDTO.getPrompt());
        input.setDuration(Integer.valueOf(happyHorseV1TextToVideoDTO.getDuration()));
        input.setResolution(happyHorseV1TextToVideoDTO.getResolution());
        input.setSeed(happyHorseV1TextToVideoDTO.getSeed());
        input.setAspectRatio(happyHorseV1TextToVideoDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/happy-house"));

        VideoGenerateResponse response = happyHorseManager.v1TextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("happy-house v1 text to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, happyHorseV1TextToVideoDTO.getPrompt(), happyHorseV1TextToVideoDTO, userModelTask, verifyCreditsBO));
    }


    @Override
    public BaseResponse v1ImageToVideo(HappyHorseV1ImageToVideoDTO happyHorseV1ImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(happyHorseV1ImageToVideoDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(happyHorseV1ImageToVideoDTO.getDuration()));
        extraData.setQuality(happyHorseV1ImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HappyHorseV1ImageToVideoRequest request = new HappyHorseV1ImageToVideoRequest();

        HappyHorseV1ImageToVideoRequest.ImageToVideoInput input = new HappyHorseV1ImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(happyHorseV1ImageToVideoDTO.getPrompt());
        input.setDuration(Integer.valueOf(happyHorseV1ImageToVideoDTO.getDuration()));
        input.setResolution(happyHorseV1ImageToVideoDTO.getResolution());
        input.setSeed(happyHorseV1ImageToVideoDTO.getSeed());
        input.setImageUrls(happyHorseV1ImageToVideoDTO.getImageUrls());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/happy-house"));

        VideoGenerateResponse response = happyHorseManager.v1ImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("happy-house v1 image to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                happyHorseV1ImageToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, happyHorseV1ImageToVideoDTO.getPrompt(), happyHorseV1ImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v1ReferenceToVideo(HappyHorseV1ReferenceToVideoDTO happyHorseV1ReferenceToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(happyHorseV1ReferenceToVideoDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(happyHorseV1ReferenceToVideoDTO.getDuration()));
        extraData.setQuality(happyHorseV1ReferenceToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HappyHorseV1ReferenceToVideoRequest request = new HappyHorseV1ReferenceToVideoRequest();

        HappyHorseV1ReferenceToVideoRequest.ReferenceToVideoInput input = new HappyHorseV1ReferenceToVideoRequest.ReferenceToVideoInput();

        input.setPrompt(happyHorseV1ReferenceToVideoDTO.getPrompt());
        input.setDuration(Integer.valueOf(happyHorseV1ReferenceToVideoDTO.getDuration()));
        input.setResolution(happyHorseV1ReferenceToVideoDTO.getResolution());
        input.setSeed(happyHorseV1ReferenceToVideoDTO.getSeed());
        input.setImageUrls(happyHorseV1ReferenceToVideoDTO.getImageUrls());
        input.setAspectRatio(happyHorseV1ReferenceToVideoDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/happy-house"));

        VideoGenerateResponse response = happyHorseManager.v1ReferenceToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("happy-house v1 reference to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                happyHorseV1ReferenceToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, happyHorseV1ReferenceToVideoDTO.getPrompt(), happyHorseV1ReferenceToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v1VideoEdit(HappyHorseV1VideoEditDTO happyHorseV1VideoEditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(happyHorseV1VideoEditDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(happyHorseV1VideoEditDTO.getDuration());
        extraData.setQuality(happyHorseV1VideoEditDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HappyHorseV1VideoEditRequest request = new HappyHorseV1VideoEditRequest();

        HappyHorseV1VideoEditRequest.VideoEditInput input = new HappyHorseV1VideoEditRequest.VideoEditInput();

        input.setPrompt(happyHorseV1VideoEditDTO.getPrompt());
        input.setResolution(happyHorseV1VideoEditDTO.getResolution());
        input.setSeed(happyHorseV1VideoEditDTO.getSeed());
        input.setAudioSetting(happyHorseV1VideoEditDTO.getAudioSetting());
        input.setVideoUrl(happyHorseV1VideoEditDTO.getVideoUrl());
        input.setReferenceImage(happyHorseV1VideoEditDTO.getReferenceImage());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/happy-house"));

        VideoGenerateResponse response = happyHorseManager.v1VideoEdit(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("happy-house v1 video edit error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(happyHorseV1VideoEditDTO.getVideoUrl());
        inputUrls.addAll(happyHorseV1VideoEditDTO.getReferenceImage());

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                inputUrls,
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, happyHorseV1VideoEditDTO.getPrompt(), happyHorseV1VideoEditDTO, userModelTask, verifyCreditsBO));
    }


}
