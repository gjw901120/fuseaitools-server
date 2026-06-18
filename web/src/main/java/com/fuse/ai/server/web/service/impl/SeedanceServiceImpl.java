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

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public BaseResponse v2(Seedance2DTO seedance2DTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedance2DTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY_SCENE);
        extraData.setDuration(Integer.valueOf(seedance2DTO.getDuration()));
        extraData.setQuality(seedance2DTO.getResolution());
        extraData.setScene(CollectionUtils.isEmpty(seedance2DTO.getReferenceVideoUrls()) ? "without_video" : "with_video");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Seedance2Request request = new Seedance2Request();

        Seedance2Request.Seedance2Input input = new Seedance2Request.Seedance2Input();

        input.setPrompt(seedance2DTO.getPrompt());
        input.setAspectRatio(seedance2DTO.getAspectRatio());
        input.setResolution(seedance2DTO.getResolution());
        input.setNsfwChecker(seedance2DTO.getNsfwChecker());
        input.setGenerateAudio(seedance2DTO.getGenerateAudio());
        input.setReferenceVideoUrls(seedance2DTO.getReferenceVideoUrls());
        input.setReferenceImageUrls(seedance2DTO.getReferenceImageUrls());
        input.setReferenceAudioUrls(seedance2DTO.getReferenceAudioUrls());
        input.setWebSearch(seedance2DTO.getWebSearch());
        input.setFirstFrameUrl(seedance2DTO.getFirstFrameUrl());
        input.setLastFrameUrl(seedance2DTO.getLastFrameUrl());
        input.setDuration(Integer.valueOf(seedance2DTO.getDuration()));


        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.v2(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance v2 to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(seedance2DTO.getReferenceVideoUrls())) {
            inputUrls.addAll(seedance2DTO.getReferenceVideoUrls());
        }
        if (!CollectionUtils.isEmpty(seedance2DTO.getReferenceImageUrls())) {
            inputUrls.addAll(seedance2DTO.getReferenceImageUrls());
        }
        if (!CollectionUtils.isEmpty(seedance2DTO.getReferenceAudioUrls())) {
            inputUrls.addAll(seedance2DTO.getReferenceAudioUrls());
        }
        if (StringUtils.hasText(seedance2DTO.getFirstFrameUrl())) {
            inputUrls.add(seedance2DTO.getFirstFrameUrl());
        }
        if (StringUtils.hasText(seedance2DTO.getLastFrameUrl())) {
            inputUrls.add(seedance2DTO.getLastFrameUrl());
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
                inputUrls,
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, seedance2DTO.getPrompt(), seedance2DTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v2Fast(Seedance2FastDTO seedance2FastDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedance2FastDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY_SCENE);
        extraData.setDuration(Integer.valueOf(seedance2FastDTO.getDuration()));
        extraData.setQuality(seedance2FastDTO.getResolution());
        List<String> referenceVideoUrls = seedance2FastDTO.getReferenceVideoUrls();
        extraData.setScene(CollectionUtils.isEmpty(referenceVideoUrls) ? "without_video" : "with_video");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Seedance2FastRequest request = new Seedance2FastRequest();

        Seedance2FastRequest.Seedance2FastInput input = new Seedance2FastRequest.Seedance2FastInput();

        input.setPrompt(seedance2FastDTO.getPrompt());
        input.setAspectRatio(seedance2FastDTO.getAspectRatio());
        input.setResolution(seedance2FastDTO.getResolution());
        input.setGenerateAudio(seedance2FastDTO.getGenerateAudio());
        input.setNsfwChecker(seedance2FastDTO.getNsfwChecker());
        input.setReferenceVideoUrls(seedance2FastDTO.getReferenceVideoUrls());
        input.setReferenceImageUrls(seedance2FastDTO.getReferenceImageUrls());
        input.setReferenceAudioUrls(seedance2FastDTO.getReferenceAudioUrls());
        input.setWebSearch(seedance2FastDTO.getWebSearch());
        input.setFirstFrameUrl(seedance2FastDTO.getFirstFrameUrl());
        input.setLastFrameUrl(seedance2FastDTO.getLastFrameUrl());
        input.setDuration(Integer.valueOf(seedance2FastDTO.getDuration()));


        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/seedance"));

        VideoGenerateResponse response = seedanceManager.v2Fast(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedance v2 fast to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(seedance2FastDTO.getReferenceVideoUrls())) {
            inputUrls.addAll(seedance2FastDTO.getReferenceVideoUrls());
        }
        if (!CollectionUtils.isEmpty(seedance2FastDTO.getReferenceImageUrls())) {
            inputUrls.addAll(seedance2FastDTO.getReferenceImageUrls());
        }
        if (!CollectionUtils.isEmpty(seedance2FastDTO.getReferenceAudioUrls())) {
            inputUrls.addAll(seedance2FastDTO.getReferenceAudioUrls());
        }
        if (StringUtils.hasText(seedance2FastDTO.getFirstFrameUrl())) {
            inputUrls.add(seedance2FastDTO.getFirstFrameUrl());
        }
        if (StringUtils.hasText(seedance2FastDTO.getLastFrameUrl())) {
            inputUrls.add(seedance2FastDTO.getLastFrameUrl());
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
                inputUrls,
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, seedance2FastDTO.getPrompt(), seedance2FastDTO, userModelTask, verifyCreditsBO));
    }

}
