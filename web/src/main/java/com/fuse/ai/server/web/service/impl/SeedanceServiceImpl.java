package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.SeedanceManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SeedanceService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SeedanceServiceImpl implements SeedanceService {

    @Autowired
    private SeedanceManager seedanceManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse liteTextToVideo(SeedanceLiteTextToVideoDTO seedanceLiteTextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedanceLiteTextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(seedanceLiteTextToVideoDTO.getDuration()));
        extraData.setQuality(seedanceLiteTextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SeedanceLiteTextToVideoRequest request = new SeedanceLiteTextToVideoRequest();

        SeedanceLiteTextToVideoRequest.LiteTextToVideoInput input = new SeedanceLiteTextToVideoRequest.LiteTextToVideoInput();

        input.setPrompt(seedanceLiteTextToVideoDTO.getPrompt());
        input.setDuration(seedanceLiteTextToVideoDTO.getDuration());
        input.setResolution(seedanceLiteTextToVideoDTO.getResolution());
        input.setCameraFixed(seedanceLiteTextToVideoDTO.getCameraFixed());
        input.setEnableSafetyChecker(seedanceLiteTextToVideoDTO.getEnableSafetyChecker());
        input.setSeed(seedanceLiteTextToVideoDTO.getSeed());
        input.setAspectRatio(seedanceLiteTextToVideoDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.liteTextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance lite text to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, seedanceLiteTextToVideoDTO.getPrompt(), seedanceLiteTextToVideoDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse liteImageToVideo(SeedanceLiteImageToVideoDTO seedanceLiteImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedanceLiteImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(seedanceLiteImageToVideoDTO.getDuration()));
        extraData.setQuality(seedanceLiteImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SeedanceLiteImageToVideoRequest request = new SeedanceLiteImageToVideoRequest();

        SeedanceLiteImageToVideoRequest.LiteImageToVideoInput input = new SeedanceLiteImageToVideoRequest.LiteImageToVideoInput();

        input.setPrompt(seedanceLiteImageToVideoDTO.getPrompt());
        input.setDuration(seedanceLiteImageToVideoDTO.getDuration());
        input.setResolution(seedanceLiteImageToVideoDTO.getResolution());
        input.setCameraFixed(seedanceLiteImageToVideoDTO.getCameraFixed());
        input.setImageUrl(seedanceLiteImageToVideoDTO.getImageUrl());
        input.setSeed(seedanceLiteImageToVideoDTO.getSeed());
        input.setEndImageUrl(seedanceLiteImageToVideoDTO.getEndImageUrl());


        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.liteImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance lite image to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(seedanceLiteImageToVideoDTO.getImageUrl());
        inputUrls.add(seedanceLiteImageToVideoDTO.getEndImageUrl());

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

        return new BaseResponse(recordsService.create(model, seedanceLiteImageToVideoDTO.getPrompt(), seedanceLiteImageToVideoDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse proTextToVideo(SeedanceProTextToVideoDTO seedanceProTextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedanceProTextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(seedanceProTextToVideoDTO.getDuration()));
        extraData.setQuality(seedanceProTextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SeedanceProTextToVideoRequest request = new SeedanceProTextToVideoRequest();

        SeedanceProTextToVideoRequest.ProTextToVideoInput input = new SeedanceProTextToVideoRequest.ProTextToVideoInput();

        input.setPrompt(seedanceProTextToVideoDTO.getPrompt());
        input.setDuration(seedanceProTextToVideoDTO.getDuration());
        input.setResolution(seedanceProTextToVideoDTO.getResolution());
        input.setCameraFixed(seedanceProTextToVideoDTO.getCameraFixed());
        input.setEnableSafetyChecker(seedanceProTextToVideoDTO.getEnableSafetyChecker());
        input.setSeed(seedanceProTextToVideoDTO.getSeed());
        input.setAspectRatio(seedanceProTextToVideoDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.proTextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance pro text to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, seedanceProTextToVideoDTO.getPrompt(), seedanceProTextToVideoDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse proImageToVideo(SeedanceProImageToVideoDTO seedanceProImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedanceProImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(seedanceProImageToVideoDTO.getDuration()));
        extraData.setQuality(seedanceProImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SeedanceProImageToVideoRequest request = new SeedanceProImageToVideoRequest();

        SeedanceProImageToVideoRequest.ProImageToVideoInput input = new SeedanceProImageToVideoRequest.ProImageToVideoInput();

        input.setPrompt(seedanceProImageToVideoDTO.getPrompt());
        input.setDuration(seedanceProImageToVideoDTO.getDuration());
        input.setResolution(seedanceProImageToVideoDTO.getResolution());
        input.setCameraFixed(seedanceProImageToVideoDTO.getCameraFixed());
        input.setImageUrl(seedanceProImageToVideoDTO.getImageUrl());
        input.setSeed(seedanceProImageToVideoDTO.getSeed());


        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.proImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance pro image to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(seedanceProImageToVideoDTO.getImageUrl());

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

        return new BaseResponse(recordsService.create(model, seedanceProImageToVideoDTO.getPrompt(), seedanceProImageToVideoDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse proFastImageToVideo(SeedanceProFastImageToVideoDTO seedanceProFastImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedanceProFastImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(seedanceProFastImageToVideoDTO.getDuration()));
        extraData.setQuality(seedanceProFastImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SeedanceProFastImageToVideoRequest request = new SeedanceProFastImageToVideoRequest();

        SeedanceProFastImageToVideoRequest.ProFastImageToVideoInput input = new SeedanceProFastImageToVideoRequest.ProFastImageToVideoInput();

        input.setPrompt(seedanceProFastImageToVideoDTO.getPrompt());
        input.setDuration(seedanceProFastImageToVideoDTO.getDuration());
        input.setResolution(seedanceProFastImageToVideoDTO.getResolution());
        input.setImageUrl(seedanceProFastImageToVideoDTO.getImageUrl());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.proFastImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance pro fast image to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(seedanceProFastImageToVideoDTO.getImageUrl());

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

        return new BaseResponse(recordsService.create(model, seedanceProFastImageToVideoDTO.getPrompt(), seedanceProFastImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse pro15GenerateVideo(Seedance15ProDTO seedance15ProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedance15ProDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY_SCENE);
        extraData.setDuration(Integer.valueOf(seedance15ProDTO.getDuration()));
        extraData.setQuality(seedance15ProDTO.getResolution());
        extraData.setScene(seedance15ProDTO.getGenerateAudio() ? "with_sound" : "without_sound");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Seedance15ProRequest request = new Seedance15ProRequest();

        Seedance15ProRequest.Seedance15ProInput input = new Seedance15ProRequest.Seedance15ProInput();

        input.setPrompt(seedance15ProDTO.getPrompt());
        input.setInputUrls(seedance15ProDTO.getInputUrls());
        input.setAspectRatio(seedance15ProDTO.getAspectRatio());
        input.setResolution(seedance15ProDTO.getResolution());
        input.setDuration(seedance15ProDTO.getDuration());
        input.setFixedLens(seedance15ProDTO.getFixedLens());
        input.setGenerateAudio(seedance15ProDTO.getGenerateAudio());
        input.setNsfwChecker(seedance15ProDTO.getNsfwChecker());


        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.pro15ToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance pro 1.5 to video error: " + response.getMsg());
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
                seedance15ProDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, seedance15ProDTO.getPrompt(), seedance15ProDTO, userModelTask, verifyCreditsBO));
    }


}
