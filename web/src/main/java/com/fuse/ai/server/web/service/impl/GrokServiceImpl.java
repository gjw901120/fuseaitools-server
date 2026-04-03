package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.GrokImagineManager;
import com.fuse.ai.server.manager.model.request.image.GrokImagineImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GrokImagineTextToImageRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineExtendRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineUpscaleRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.GrokImagineImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GrokImagineTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineTextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineUpscaleDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.GrokService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class GrokServiceImpl implements GrokService {

    @Autowired
    private GrokImagineManager grokImagineManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse textToImage(GrokImagineTextToImageDTO grokImagineTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineTextToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        GrokImagineTextToImageRequest request = new GrokImagineTextToImageRequest();
        GrokImagineTextToImageRequest.TextToImageInput input = new GrokImagineTextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setAspectRatio(grokImagineTextToImageDTO.getAspectRatio());
        input.setPrompt(grokImagineTextToImageDTO.getPrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/grok"));
        request.setInput(input);

        ImageGenerateResponse response = grokImagineManager.textToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("grok text to image error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, grokImagineTextToImageDTO.getPrompt(), grokImagineTextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToImage(GrokImagineImageToImageDTO grokImagineImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineImageToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        GrokImagineImageToImageRequest request = new GrokImagineImageToImageRequest();
        GrokImagineImageToImageRequest.ImageToImageInput input = new GrokImagineImageToImageRequest.ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setImageUrls(grokImagineImageToImageDTO.getImageUrls());
        input.setPrompt(grokImagineImageToImageDTO.getPrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/grok"));
        request.setInput(input);

        ImageGenerateResponse response = grokImagineManager.imageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("grok image to image error: " + response.getMessage());
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
                grokImagineImageToImageDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, grokImagineImageToImageDTO.getPrompt(), grokImagineImageToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse textToVideo(GrokImagineTextToVideoDTO grokImagineTextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineTextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(grokImagineTextToVideoDTO.getDuration().intValue());
        extraData.setQuality(grokImagineTextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GrokImagineTextToVideoRequest request = new GrokImagineTextToVideoRequest();

        GrokImagineTextToVideoRequest.TextToVideoInput input = new GrokImagineTextToVideoRequest.TextToVideoInput();

        input.setPrompt(grokImagineTextToVideoDTO.getPrompt());
        input.setDuration(grokImagineTextToVideoDTO.getDuration());
        input.setResolution(grokImagineTextToVideoDTO.getResolution());
        input.setMode(grokImagineTextToVideoDTO.getMode());
        input.setAspectRatio(grokImagineTextToVideoDTO.getAspectRatio());
        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/grok"));

        VideoGenerateResponse response = grokImagineManager.textToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("grok text to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, grokImagineTextToVideoDTO.getPrompt(), grokImagineTextToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToVideo(GrokImagineImageToVideoDTO grokImagineImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(grokImagineImageToVideoDTO.getDuration().intValue());
        extraData.setQuality(grokImagineImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GrokImagineImageToVideoRequest request = new GrokImagineImageToVideoRequest();

        GrokImagineImageToVideoRequest.ImageToVideoInput input = new GrokImagineImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(grokImagineImageToVideoDTO.getPrompt());
        input.setDuration(grokImagineImageToVideoDTO.getDuration());
        input.setResolution(grokImagineImageToVideoDTO.getResolution());
        input.setImageUrls(grokImagineImageToVideoDTO.getImageUrls());
        input.setMode(grokImagineImageToVideoDTO.getMode());
        input.setAspectRatio(grokImagineImageToVideoDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/grok"));

        VideoGenerateResponse response = grokImagineManager.imageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan image to video error: " + response.getMsg());
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
                grokImagineImageToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, grokImagineImageToVideoDTO.getPrompt(), grokImagineImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse upscale(GrokImagineUpscaleDTO grokImagineUpscaleDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineUpscaleDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        GrokImagineUpscaleRequest request = new GrokImagineUpscaleRequest();

        GrokImagineUpscaleRequest.UpscaleInput input = new GrokImagineUpscaleRequest.UpscaleInput();

        input.setTaskId(grokImagineUpscaleDTO.getTaskId());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/grok"));

        VideoGenerateResponse response = grokImagineManager.upscale(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Grok upscale error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, "grok upscale", grokImagineUpscaleDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse extend(GrokImagineExtendDTO grokImagineExtendDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(grokImagineExtendDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION);
        extraData.setDuration(Integer.valueOf(grokImagineExtendDTO.getExtendTimes()));
//        extraData.setQuality("720p");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GrokImagineExtendRequest request = new GrokImagineExtendRequest();

        GrokImagineExtendRequest.ExtendInput input = new GrokImagineExtendRequest.ExtendInput();

        input.setTaskId(grokImagineExtendDTO.getTaskId());
        input.setExtendAt(grokImagineExtendDTO.getExtendAt().doubleValue());
        input.setExtendTimes(grokImagineExtendDTO.getExtendTimes());
        input.setPrompt(grokImagineExtendDTO.getPrompt());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/grok"));

        VideoGenerateResponse response = grokImagineManager.extend(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Grok extend error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, grokImagineExtendDTO.getPrompt(), grokImagineExtendDTO, userModelTask, verifyCreditsBO));
    }
}
