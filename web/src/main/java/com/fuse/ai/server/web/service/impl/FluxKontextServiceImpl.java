package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.image.Flux2ImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.Flux2ProTextToImageRequest;
import com.fuse.ai.server.manager.model.request.image.Flux2TextToImageRequest;
import com.fuse.ai.server.manager.model.request.image.FluxKontextImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.FluxKontextService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
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
public class FluxKontextServiceImpl implements FluxKontextService {

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse fluxKontextGenerate(FluxKontextGenerateDTO fluxKontextGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(fluxKontextGenerateDTO.getModel().getCode());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        FluxKontextImageRequest request = new FluxKontextImageRequest();

        BeanUtils.copyProperties(fluxKontextGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();

        request.setInputImage(fluxKontextGenerateDTO.getImageUrl());
        inputUrls.add(fluxKontextGenerateDTO.getImageUrl());

        request.setCallBackUrl(callbackUrl.concat("/image/flux-kontext"));

        ImageGenerateResponse response = imageManager.fluxKontextGenerate(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("FluxKontext generate error: " + response.getMessage());
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
                inputUrls,
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), fluxKontextGenerateDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse flux2ImageToImage(Flux2ImageToImageDTO flux2ImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(flux2ImageToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(flux2ImageToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现生成逻辑
        Flux2ImageToImageRequest request = new Flux2ImageToImageRequest();
        Flux2ImageToImageRequest.Flux2ImageToImageInput input = new Flux2ImageToImageRequest.Flux2ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(flux2ImageToImageDTO.getPrompt());
        input.setResolution(flux2ImageToImageDTO.getResolution());
        input.setAspectRatio(flux2ImageToImageDTO.getAspectRatio());
        input.setInputUrls(flux2ImageToImageDTO.getInputUrls());
        input.setNsfwChecker(flux2ImageToImageDTO.getNsfwChecker());
        request.setCallBackUrl(callbackUrl.concat("/image/flux"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.flux2ImageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Flux 2 image to image error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                flux2ImageToImageDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, flux2ImageToImageDTO.getPrompt(), flux2ImageToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse flux2ProImageToImage(Flux2ProImageToImageDTO flux2ProImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(flux2ProImageToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(flux2ProImageToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现生成逻辑
        Flux2ImageToImageRequest request = new Flux2ImageToImageRequest();
        Flux2ImageToImageRequest.Flux2ImageToImageInput input = new Flux2ImageToImageRequest.Flux2ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(flux2ProImageToImageDTO.getPrompt());
        input.setResolution(flux2ProImageToImageDTO.getResolution());
        input.setAspectRatio(flux2ProImageToImageDTO.getAspectRatio());
        input.setInputUrls(flux2ProImageToImageDTO.getInputUrls());
        input.setNsfwChecker(flux2ProImageToImageDTO.getNsfwChecker());
        request.setCallBackUrl(callbackUrl.concat("/image/flux"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.flux2ImageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Flux 2 pro image to image error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                flux2ProImageToImageDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, flux2ProImageToImageDTO.getPrompt(), flux2ProImageToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse flux2ProTextToImage(Flux2ProTextToImageDTO flux2ProTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(flux2ProTextToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(flux2ProTextToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现生成逻辑
        Flux2ProTextToImageRequest request = new Flux2ProTextToImageRequest();
        Flux2ProTextToImageRequest.Flux2ProTextToImageInput input = new Flux2ProTextToImageRequest.Flux2ProTextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(flux2ProTextToImageDTO.getPrompt());
        input.setResolution(flux2ProTextToImageDTO.getResolution());
        input.setAspectRatio(flux2ProTextToImageDTO.getAspectRatio());
        input.setNsfwChecker(flux2ProTextToImageDTO.getNsfwChecker());
        request.setCallBackUrl(callbackUrl.concat("/image/flux"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.flux2ProTextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Flux 2 pro text to image error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, flux2ProTextToImageDTO.getPrompt(), flux2ProTextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse flux2TextToImage(Flux2TextToImageDTO flux2TextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(flux2TextToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(flux2TextToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现生成逻辑
        Flux2TextToImageRequest request = new Flux2TextToImageRequest();
        Flux2TextToImageRequest.Flux2TextToImageInput input = new Flux2TextToImageRequest.Flux2TextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(flux2TextToImageDTO.getPrompt());
        input.setResolution(flux2TextToImageDTO.getResolution());
        input.setAspectRatio(flux2TextToImageDTO.getAspectRatio());
        input.setNsfwChecker(flux2TextToImageDTO.getNsfwChecker());
        request.setCallBackUrl(callbackUrl.concat("/image/flux"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.flux2TextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Flux 2 text to image error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, flux2TextToImageDTO.getPrompt(), flux2TextToImageDTO, userModelTask, verifyCreditsBO));
    }

}
