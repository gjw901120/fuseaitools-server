package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.*;
import com.fuse.ai.server.manager.manager.SoraManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
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
        request.setModel(SoraModelEnum.fromCode(soraGenerateDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        List<String> inputUrls = new ArrayList<>();

        if(soraGenerateDTO.getModel().equals(SoraModelEnum.SORA_2_TEXT_TO_VIDEO.getCode())) {
            SoraTextToVideoRequest soraRequest = new SoraTextToVideoRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.fromCode(soraGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraGenerateDTO.getPrompt());
            soraRequest.setNFrames(soraGenerateDTO.getNFrames());
            soraRequest.setRemoveWatermark(soraGenerateDTO.getRemoveWatermark());
            request.setInput(soraRequest);
        } else {
            SoraImageToVideoRequestRequest soraRequest = new SoraImageToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.fromCode(soraGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraGenerateDTO.getPrompt());
            soraRequest.setNFrames(soraGenerateDTO.getNFrames());
            soraRequest.setRemoveWatermark(soraGenerateDTO.getRemoveWatermark());
            soraRequest.setImageUrls(soraGenerateDTO.getImageUrls());
            if (!CollectionUtils.isEmpty(soraRequest.getImageUrls())) {
                inputUrls.addAll(soraRequest.getImageUrls());
            }
            request.setInput(soraRequest);
        }
        VideoGenerateResponse response = soraManager.generateVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Sora generate error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

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

        return new BaseResponse(recordsService.create(model, soraGenerateDTO.getPrompt(), soraGenerateDTO, userModelTask, verifyCreditsBO));

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
        request.setModel(SoraModelEnum.fromCode(soraProGenerateDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        List<String> inputUrls = new ArrayList<>();

        if(soraProGenerateDTO.getModel().equals(SoraModelEnum.SORA_2_PRO_TEXT_TO_VIDEO.getCode())) {
            SoraProTextToVideoRequestRequest soraRequest = new SoraProTextToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.fromCode(soraProGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraProGenerateDTO.getPrompt());
            soraRequest.setNFrames(soraProGenerateDTO.getNFrames());
            soraRequest.setRemoveWatermark(soraProGenerateDTO.getRemoveWatermark());
            soraRequest.setSize(SoraSizeEnum.fromCode(soraProGenerateDTO.getSize()));
            request.setInput(soraRequest);
        } else {
            SoraProImageToVideoRequestRequest soraRequest = new SoraProImageToVideoRequestRequest();
            soraRequest.setAspectRatio(SoraAspectRatioEnum.fromCode(soraProGenerateDTO.getAspectRatio()));
            soraRequest.setPrompt(soraProGenerateDTO.getPrompt());
            soraRequest.setNFrames(soraProGenerateDTO.getNFrames());
            soraRequest.setRemoveWatermark(soraProGenerateDTO.getRemoveWatermark());
            soraRequest.setSize(SoraSizeEnum.fromCode(soraProGenerateDTO.getSize()));
            soraRequest.setImageUrls(soraProGenerateDTO.getImageUrls());
            if (!CollectionUtils.isEmpty(soraRequest.getImageUrls())) {
                inputUrls.addAll(soraRequest.getImageUrls());
            }
            request.setInput(soraRequest);
        }

        VideoGenerateResponse response = soraManager.generateVideo(request, model.getRequestToken());
        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Sora generate error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
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

        return new BaseResponse(recordsService.create(model, soraProGenerateDTO.getPrompt(), soraProGenerateDTO, userModelTask, verifyCreditsBO));

    }


    @Override
    public BaseResponse soraWatermarkRemover(SoraWatermarkRemoverDTO soraWatermarkRemoverDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(soraWatermarkRemoverDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SoraGenerateRequest request = new SoraGenerateRequest();

        SoraWatermarkRemoverRequest soraRequest = new SoraWatermarkRemoverRequest();

        List<String> inputUrls = new ArrayList<>();

        request.setModel(SoraModelEnum.fromCode(soraWatermarkRemoverDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        soraRequest.setVideoUrl(soraWatermarkRemoverDTO.getVideoUrl());

        request.setInput(soraRequest);
        inputUrls.add(soraWatermarkRemoverDTO.getVideoUrl());

        VideoGenerateResponse response = soraManager.soraWatermarkRemover(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Sora watermark remover error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }
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

        return new BaseResponse(recordsService.create(model, "watermark remover", soraWatermarkRemoverDTO, userModelTask, verifyCreditsBO));

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

        List<String> inputUrls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(soraProStoryboardDTO.getImageUrls())) {
            inputUrls.add(soraProStoryboardDTO.getImageUrls().get(0));
        }

        soraRequest.setAspectRatio(SoraAspectRatioEnum.fromCode(soraProStoryboardDTO.getAspectRatio()));
        soraRequest.setNFrames(soraProStoryboardDTO.getNFrames());

        soraRequest.setImageUrls(soraProStoryboardDTO.getImageUrls());

        List<SoraStoryboardSceneRequest> shots = new ArrayList<>();

        BeanUtils.copyProperties(shots, soraProStoryboardDTO.getShots());

        for (SoraProStoryboardDTO.Shot shot : soraProStoryboardDTO.getShots()) {
            SoraStoryboardSceneRequest shotRequest = new SoraStoryboardSceneRequest();
            shotRequest.setDuration(shot.getDuration());
            shotRequest.setScene(shot.getScene());
            shots.add(shotRequest);
        }

        soraRequest.setShots(shots);

        request.setModel(SoraModelEnum.fromCode(soraProStoryboardDTO.getModel()));
        request.setCallBackUrl(callbackUrl.concat("/video/sora"));

        request.setInput(soraRequest);

        VideoGenerateResponse response = soraManager.soraStoryboard(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Sora pro story board error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
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

        return new BaseResponse(recordsService.create(model, "Storyboard mode", soraProStoryboardDTO, userModelTask, verifyCreditsBO));

    }
}
