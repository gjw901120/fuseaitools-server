package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.QwenManager;
import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.QwenService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;


@Service
public class QwenServiceImpl implements QwenService {

    @Autowired
    private QwenManager qwenManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse textToImage(QwenTextToImageDTO qwenTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenTextToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        QwenTextToImageRequest request = new QwenTextToImageRequest();
        QwenTextToImageRequest.TextToImageInput input = new QwenTextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setOutputFormat(qwenTextToImageDTO.getOutputFormat());
        input.setPrompt(qwenTextToImageDTO.getPrompt());
        input.setImageSize(qwenTextToImageDTO.getImageSize());
        input.setNumInferenceSteps(qwenTextToImageDTO.getNumInferenceSteps());
        input.setSeed(qwenTextToImageDTO.getSeed());
        input.setGuidanceScale(qwenTextToImageDTO.getGuidanceScale());
        input.setEnableSafetyChecker(qwenTextToImageDTO.getEnableSafetyChecker());
        input.setNegativePrompt(qwenTextToImageDTO.getNegativePrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.textToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen textToImage error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenTextToImageDTO.getPrompt(), qwenTextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToImage(QwenImageToImageDTO qwenImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenImageToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        QwenImageToImageRequest request = new QwenImageToImageRequest();
        QwenImageToImageRequest.ImageToImageInput input = new QwenImageToImageRequest.ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setOutputFormat(qwenImageToImageDTO.getOutputFormat());
        input.setPrompt(qwenImageToImageDTO.getPrompt());
        input.setNumInferenceSteps(qwenImageToImageDTO.getNumInferenceSteps());
        input.setSeed(qwenImageToImageDTO.getSeed());
        input.setGuidanceScale(qwenImageToImageDTO.getGuidanceScale());
        input.setEnableSafetyChecker(qwenImageToImageDTO.getEnableSafetyChecker());
        input.setNegativePrompt(qwenImageToImageDTO.getNegativePrompt());
        input.setImageUrl(qwenImageToImageDTO.getImageUrl());
        input.setStrength(qwenImageToImageDTO.getStrength());
        input.setAcceleration(qwenImageToImageDTO.getAcceleration());
        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.imageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen imageToImage error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(qwenImageToImageDTO.getImageUrl());

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenImageToImageDTO.getPrompt(), qwenImageToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageEdit(QwenImageEditDTO qwenImageEditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenImageEditDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        QwenImageEditRequest request = new QwenImageEditRequest();
        QwenImageEditRequest.ImageEditInput input = new QwenImageEditRequest.ImageEditInput();

        request.setModel(model.getRequestName());
        input.setOutputFormat(qwenImageEditDTO.getOutputFormat());
        input.setPrompt(qwenImageEditDTO.getPrompt());
        input.setNumInferenceSteps(qwenImageEditDTO.getNumInferenceSteps());
        input.setSeed(qwenImageEditDTO.getSeed());
        input.setGuidanceScale(qwenImageEditDTO.getGuidanceScale());
        input.setEnableSafetyChecker(qwenImageEditDTO.getEnableSafetyChecker());
        input.setNegativePrompt(qwenImageEditDTO.getNegativePrompt());
        input.setImageUrl(qwenImageEditDTO.getImageUrl());
        input.setAcceleration(qwenImageEditDTO.getAcceleration());
        input.setNumImages(qwenImageEditDTO.getNumImages());
        input.setSyncMode(qwenImageEditDTO.getSyncMode());
        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.imageEdit(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen imageEdit error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(qwenImageEditDTO.getImageUrl());

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenImageEditDTO.getPrompt(), qwenImageEditDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse zImage(QwenZImageDTO qwenZImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenZImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        QwenZImageRequest request = new QwenZImageRequest();
        QwenZImageRequest.ZImageInput input = new QwenZImageRequest.ZImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(qwenZImageDTO.getPrompt());
        input.setAspectRatio(qwenZImageDTO.getAspectRatio());
        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.zImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen zImage error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenZImageDTO.getPrompt(), qwenZImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v2TextToImage(Qwen2TextToImageDTO qwenTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenTextToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        Qwen2TextToImageRequest request = new Qwen2TextToImageRequest();
        Qwen2TextToImageRequest.TextToImageInput input = new Qwen2TextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setOutputFormat(qwenTextToImageDTO.getOutputFormat());
        input.setPrompt(qwenTextToImageDTO.getPrompt());
        input.setImageSize(qwenTextToImageDTO.getImageSize());
        input.setSeed(qwenTextToImageDTO.getSeed());
        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.v2TextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen v2TextToImage error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenTextToImageDTO.getPrompt(), qwenTextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v2ImageEdit(Qwen2ImageEditDTO qwenImageEditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(qwenImageEditDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        Qwen2ImageEditRequest request = new Qwen2ImageEditRequest();
        Qwen2ImageEditRequest.ImageEditInput input = new Qwen2ImageEditRequest.ImageEditInput();

        request.setModel(model.getRequestName());
        input.setOutputFormat(qwenImageEditDTO.getOutputFormat());
        input.setImageUrl(qwenImageEditDTO.getImageUrl());
        input.setImageSize(qwenImageEditDTO.getImageSize());
        input.setSeed(qwenImageEditDTO.getSeed());
        input.setPrompt(qwenImageEditDTO.getPrompt());

        request.setCallBackUrl(callbackUrl.concat("/image/qwen"));
        request.setInput(input);

        ImageGenerateResponse response = qwenManager.v2ImageEdit(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Qwen v2ImageEdit error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(qwenImageEditDTO.getImageUrl());

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, qwenImageEditDTO.getPrompt(), qwenImageEditDTO, userModelTask, verifyCreditsBO));
    }
}
