package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.image.Imagen4FastRequest;
import com.fuse.ai.server.manager.model.request.image.Imagen4GenerateRequest;
import com.fuse.ai.server.manager.model.request.image.Imagen4UltraRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.Imagen4FastDTO;
import com.fuse.ai.server.web.model.dto.request.image.Imagen4GenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.Imagen4UltraDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ImagenService;
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
public class ImagenServiceImpl implements ImagenService {

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
    public BaseResponse generate(Imagen4GenerateDTO imagen4GenerateDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(imagen4GenerateDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现生成逻辑
        Imagen4GenerateRequest request = new Imagen4GenerateRequest();
        Imagen4GenerateRequest.Imagen4GenerateInput input = new Imagen4GenerateRequest.Imagen4GenerateInput();

        request.setModel(model.getRequestName());
        input.setPrompt(imagen4GenerateDTO.getPrompt());
        input.setAspectRatio(imagen4GenerateDTO.getAspectRatio());
        input.setSeed(imagen4GenerateDTO.getSeed());
        input.setNegativePrompt(imagen4GenerateDTO.getNegativePrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/imagen"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.imagen4Generate(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("imagen4 generate error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, imagen4GenerateDTO.getPrompt(), imagen4GenerateDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse fastGenerate(Imagen4FastDTO imagen4FastDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(imagen4FastDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现生成逻辑
        Imagen4FastRequest request = new Imagen4FastRequest();
        Imagen4FastRequest.Imagen4FastInput input = new Imagen4FastRequest.Imagen4FastInput();

        request.setModel(model.getRequestName());
        input.setPrompt(imagen4FastDTO.getPrompt());
        input.setAspectRatio(imagen4FastDTO.getAspectRatio());
        input.setSeed(imagen4FastDTO.getSeed());
        input.setNegativePrompt(imagen4FastDTO.getNegativePrompt());
        input.setNumImages(imagen4FastDTO.getNumImages());
        request.setCallBackUrl(callbackUrl.concat("/image/imagen"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.imagen4Fast(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("imagen4 fast error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, imagen4FastDTO.getPrompt(), imagen4FastDTO, userModelTask, verifyCreditsBO));
    }


    @Override
    public BaseResponse ultraGenerate(Imagen4UltraDTO imagen4UltraDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(imagen4UltraDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现生成逻辑
        Imagen4UltraRequest request = new Imagen4UltraRequest();
        Imagen4UltraRequest.Imagen4UltraInput input = new Imagen4UltraRequest.Imagen4UltraInput();

        request.setModel(model.getRequestName());
        input.setPrompt(imagen4UltraDTO.getPrompt());
        input.setAspectRatio(imagen4UltraDTO.getAspectRatio());
        input.setSeed(imagen4UltraDTO.getSeed());
        input.setNegativePrompt(imagen4UltraDTO.getNegativePrompt());
        request.setCallBackUrl(callbackUrl.concat("/image/imagen"));

        request.setInput(input);

        ImageGenerateResponse response = imageManager.imagen4Ultra(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("imagen4 ultra error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, imagen4UltraDTO.getPrompt(), imagen4UltraDTO, userModelTask, verifyCreditsBO));
    }

}
