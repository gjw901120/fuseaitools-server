package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.GptImageManager;
import com.fuse.ai.server.manager.model.request.image.GptImageImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageTextToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2ImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2TextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageV2ImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageV2TextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.GptImageService;
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

@Slf4j
@Service
public class GptImageServiceImpl implements GptImageService {

    @Autowired
    private GptImageManager gptImageManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse textToImage(GptImageTextToImageDTO gptImageTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(gptImageTextToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SIZE);
        extraData.setSize(gptImageTextToImageDTO.getQuality());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GptImageTextToImageRequest request = new GptImageTextToImageRequest();
        GptImageTextToImageRequest.TextToImageInput input = new GptImageTextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(gptImageTextToImageDTO.getPrompt());
        input.setAspectRatio(gptImageTextToImageDTO.getAspectRatio());
        input.setQuality(gptImageTextToImageDTO.getQuality());
        request.setCallBackUrl(callbackUrl.concat("/image/gpt-image"));
        request.setInput(input);

        ImageGenerateResponse response = gptImageManager.gptImageTextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("GptImage text to image error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, gptImageTextToImageDTO.getPrompt(), gptImageTextToImageDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse imageToImage(GptImageImageToImageDTO gptImageImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(gptImageImageToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SIZE);
        extraData.setSize(gptImageImageToImageDTO.getQuality());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GptImageImageToImageRequest request = new GptImageImageToImageRequest();
        GptImageImageToImageRequest.ImageToImageInput input = new GptImageImageToImageRequest.ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(gptImageImageToImageDTO.getPrompt());
        input.setAspectRatio(gptImageImageToImageDTO.getAspectRatio());
        input.setQuality(gptImageImageToImageDTO.getQuality());
        input.setInputUrls(gptImageImageToImageDTO.getInputUrls());
        request.setCallBackUrl(callbackUrl.concat("/image/gpt-image"));
        request.setInput(input);

        ImageGenerateResponse response = gptImageManager.gptImageImageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("GptImage image to image error: " + response.getMessage());
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
                gptImageImageToImageDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, gptImageImageToImageDTO.getPrompt(), gptImageImageToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v2TextToImage(GptImageV2TextToImageDTO gptImageV2TextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(gptImageV2TextToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(gptImageV2TextToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GptImageV2TextToImageRequest request = new GptImageV2TextToImageRequest();
        GptImageV2TextToImageRequest.TextToImageInput input = new GptImageV2TextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(gptImageV2TextToImageDTO.getPrompt());
        input.setAspectRatio(gptImageV2TextToImageDTO.getAspectRatio());
        input.setResolution(gptImageV2TextToImageDTO.getResolution());
        request.setCallBackUrl(callbackUrl.concat("/image/gpt-image"));
        request.setInput(input);

        ImageGenerateResponse response = gptImageManager.gptImageV2TextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("GptImage v2 text to image error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, gptImageV2TextToImageDTO.getPrompt(), gptImageV2TextToImageDTO, userModelTask, verifyCreditsBO));
    }
    @Override
    public BaseResponse v2ImageToImage(GptImageV2ImageToImageDTO gptImageV2ImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(gptImageV2ImageToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(gptImageV2ImageToImageDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        GptImageV2ImageToImageRequest request = new GptImageV2ImageToImageRequest();
        GptImageV2ImageToImageRequest.ImageToImageInput input = new GptImageV2ImageToImageRequest.ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(gptImageV2ImageToImageDTO.getPrompt());
        input.setAspectRatio(gptImageV2ImageToImageDTO.getAspectRatio());
        input.setResolution(gptImageV2ImageToImageDTO.getResolution());
        input.setInputUrls(gptImageV2ImageToImageDTO.getInputUrls());
        request.setCallBackUrl(callbackUrl.concat("/image/gpt-image"));
        request.setInput(input);

        ImageGenerateResponse response = gptImageManager.gptImageV2ImageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("GptImage v2 image to image error: " + response.getMessage());
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
                gptImageV2ImageToImageDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, gptImageV2ImageToImageDTO.getPrompt(), gptImageV2ImageToImageDTO, userModelTask, verifyCreditsBO));
    }

}
