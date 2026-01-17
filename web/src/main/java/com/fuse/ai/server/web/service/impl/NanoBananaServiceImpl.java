package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.NanoBananaAspectRatioEnum;
import com.fuse.ai.server.manager.enums.NanoBananaOutputFormatEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.NanoBananaEditRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaGenerateRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaProGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.NanoBananaService;
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
public class NanoBananaServiceImpl implements NanoBananaService {

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
    public BaseResponse nanoBananaGenerate(NanoBananaGenerateDTO nanoBananaGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(nanoBananaGenerateDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        NanoBananaGenerateRequest request = new NanoBananaGenerateRequest();
        NanoBananaGenerateRequest.Input input = new NanoBananaGenerateRequest.Input();

        request.setModel(model.getRequestName());
        input.setOutputFormat(NanoBananaOutputFormatEnum.getByFormat(nanoBananaGenerateDTO.getOutputFormat()));
        input.setPrompt(nanoBananaGenerateDTO.getPrompt());
        input.setImageSize(NanoBananaAspectRatioEnum.getByRatio(nanoBananaGenerateDTO.getImageSize()));
        request.setCallBackUrl(callbackUrl.concat("/image/nano-banana"));
        request.setInput(input);

        ImageGenerateResponse response = imageManager.nanoBananaGenerate(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, nanoBananaGenerateDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse nanoBananaEdit(NanoBananaEditDTO nanoBananaEditDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(nanoBananaEditDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现生成逻辑
        NanoBananaEditRequest request = new NanoBananaEditRequest();
        NanoBananaEditRequest.Input  input = new NanoBananaEditRequest.Input();

        request.setModel(model.getRequestName());
        input.setImageSize(NanoBananaAspectRatioEnum.getByRatio(nanoBananaEditDTO.getImageSize()));
        input.setImageUrls(nanoBananaEditDTO.getImageUrls());
        input.setOutputFormat(NanoBananaOutputFormatEnum.getByFormat(nanoBananaEditDTO.getOutputFormat()));
        input.setPrompt(nanoBananaEditDTO.getPrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/nano-banana"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.nanoBananaEdit(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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
                nanoBananaEditDTO.getImageUrls(),
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, nanoBananaEditDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse nanoBananaProGenerate(NanoBananaProGenerateDTO nanoBananaProGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(nanoBananaProGenerateDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.QUALITY);
        extraData.setQuality(nanoBananaProGenerateDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现生成逻辑
        NanoBananaProGenerateRequest request = new NanoBananaProGenerateRequest();
        NanoBananaProGenerateRequest.Input  input = new NanoBananaProGenerateRequest.Input();

        request.setModel(model.getRequestName());
        input.setImageSize(NanoBananaAspectRatioEnum.getByRatio(nanoBananaProGenerateDTO.getImageSize()));
        input.setImageInput(nanoBananaProGenerateDTO.getImageInput());
        input.setOutputFormat(NanoBananaOutputFormatEnum.getByFormat(nanoBananaProGenerateDTO.getOutputFormat()));
        input.setPrompt(nanoBananaProGenerateDTO.getPrompt());
        input.setResolution(nanoBananaProGenerateDTO.getResolution());
        request.setCallBackUrl(callbackUrl.concat("/image/nano-banana"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.nanoBananaProGenerate(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
        }

        //写入任务
        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                verifyCreditsBO.getPricingRulesId(),
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                nanoBananaProGenerateDTO.getImageInput(),
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, nanoBananaProGenerateDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

}
