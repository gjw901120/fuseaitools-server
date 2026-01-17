package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.*;
import com.fuse.ai.server.manager.manager.SoraManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProStoryboardDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraWatermarkRemoverDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SoraGenerateService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SoraGenerateServiceImpl implements SoraGenerateService {

    @Autowired
    private SoraManager soraManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse soraGenerate(SoraGenerateDTO soraGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(soraGenerateDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SoraGenerateRequest request = new SoraGenerateRequest();
        request.setModel(SoraModelEnum.getByCode(soraGenerateDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        List<String> inputUrls = new ArrayList<>();

        if(soraGenerateDTO.getModel().equals(SoraModelEnum.SORA_2_TEXT_TO_VIDEO.getCode())) {
            SoraTextToVideoRequest soraRequest = new SoraTextToVideoRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.getByCode(soraGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraGenerateDTO.getPrompt());
            soraRequest.setNFrames(SoraFramesEnum.getByCode(soraGenerateDTO.getNFrames()));
            soraRequest.setRemoveWatermark(soraGenerateDTO.getRemoveWatermark());
            request.setInput(soraRequest);
        } else {
            SoraImageToVideoRequestRequest soraRequest = new SoraImageToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.getByCode(soraGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraGenerateDTO.getPrompt());
            soraRequest.setNFrames(SoraFramesEnum.getByCode(soraGenerateDTO.getNFrames()));
            soraRequest.setRemoveWatermark(soraGenerateDTO.getRemoveWatermark());
            soraRequest.setImageUrls(soraGenerateDTO.getImageUrls());
            inputUrls.addAll(soraRequest.getImageUrls());
            request.setInput(soraRequest);
        }

        VideoGenerateResponse response = soraManager.generateVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
        }

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

        return new BaseResponse(recordsService.create(model, soraGenerateDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }


    @Override
    public BaseResponse soraProGenerate(SoraProGenerateDTO soraProGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(soraProGenerateDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_SIZE);
        extraData.setDuration(Integer.valueOf(soraProGenerateDTO.getNFrames()));
        extraData.setSize(soraProGenerateDTO.getSize());

        verifyCreditsBO verifyCreditsBO =  userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        SoraGenerateRequest request = new SoraGenerateRequest();
        request.setModel(SoraModelEnum.getByCode(soraProGenerateDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        List<String> inputUrls = new ArrayList<>();

        if(soraProGenerateDTO.getModel().equals(SoraModelEnum.SORA_2_PRO_TEXT_TO_VIDEO.getCode())) {
            SoraProTextToVideoRequestRequest soraRequest = new SoraProTextToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.getByCode(soraProGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraProGenerateDTO.getPrompt());
            soraRequest.setNFrames(SoraFramesEnum.getByCode(soraProGenerateDTO.getNFrames()));
            soraRequest.setRemoveWatermark(soraProGenerateDTO.getRemoveWatermark());
            soraRequest.setSize(SoraSizeEnum.getByCode(soraProGenerateDTO.getSize()));
            request.setInput(soraRequest);
        } else {
            SoraProImageToVideoRequestRequest soraRequest = new SoraProImageToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.getByCode(soraProGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraProGenerateDTO.getPrompt());
            soraRequest.setNFrames(SoraFramesEnum.getByCode(soraProGenerateDTO.getNFrames()));
            soraRequest.setRemoveWatermark(soraProGenerateDTO.getRemoveWatermark());
            soraRequest.setSize(SoraSizeEnum.getByCode(soraProGenerateDTO.getSize()));
            soraRequest.setImageUrls(soraProGenerateDTO.getImageUrls());
            inputUrls.addAll(soraRequest.getImageUrls());
            request.setInput(soraRequest);
        }

        VideoGenerateResponse response = soraManager.generateVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
        }

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                inputUrls,
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, soraProGenerateDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }


    @Override
    public BaseResponse soraWatermarkRemover(SoraWatermarkRemoverDTO soraWatermarkRemoverDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(soraWatermarkRemoverDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SoraGenerateRequest request = new SoraGenerateRequest();

        SoraWatermarkRemoverRequest soraRequest = new SoraWatermarkRemoverRequest();

        List<String> inputUrls = new ArrayList<>();

        request.setModel(SoraModelEnum.getByCode(soraWatermarkRemoverDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        soraRequest.setVideoUrl(soraWatermarkRemoverDTO.getVideoUrl());

        request.setInput(soraRequest);
        inputUrls.add(soraWatermarkRemoverDTO.getVideoUrl());

        VideoGenerateResponse response = soraManager.soraWatermarkRemover(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
        }

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

        return new BaseResponse(recordsService.create(model, "watermark remover", userModelTask, verifyCreditsBO));

    }


    @Override
    public BaseResponse soraProStoryboard(SoraProStoryboardDTO soraProStoryboardDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(soraProStoryboardDTO.getModel());

        ExtraDataBO extraDataBO = new  ExtraDataBO();
        extraDataBO.setType(ExtraDataEnum.DURATION);
        extraDataBO.setDuration(Integer.valueOf(soraProStoryboardDTO.getNFrames()));

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraDataBO);

        // 实现视频生成逻辑
        SoraGenerateRequest request = new SoraGenerateRequest();

        SoraStoryboardRequest soraRequest = new SoraStoryboardRequest();

        List<String> inputUrls = new ArrayList<>(soraRequest.getImageUrls());

        soraRequest.setAspectRatio(SoraAspectRatioEnum.getByCode(soraProStoryboardDTO.getAspectRatio()));
        soraRequest.setNFrames(SoraFramesEnum.getByCode(soraProStoryboardDTO.getNFrames()));

        soraRequest.setImageUrls(soraProStoryboardDTO.getImageUrls());

        List<SoraStoryboardSceneRequest> shots = new ArrayList<>();

        BeanUtils.copyProperties(shots, soraProStoryboardDTO.getShots());

        soraRequest.setShots(shots);

        request.setModel(SoraModelEnum.getByCode(soraProStoryboardDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        request.setInput(soraRequest);

        VideoGenerateResponse response = soraManager.soraStoryboard(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
        }

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                inputUrls,
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, "Storyboard mode", userModelTask, verifyCreditsBO));

    }
}
