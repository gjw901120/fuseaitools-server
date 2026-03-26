package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.IdeogramManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.IdeogramService;
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
import java.util.List;

@Service
@Slf4j
public class IdeogramServiceImpl implements IdeogramService {

    @Autowired
    private IdeogramManager ideogramManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse v3TextToImage(IdeogramV3TextToImageDTO ideogramV3TextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramV3TextToImageDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramV3TextToImageDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramV3TextToImageRequest request = new IdeogramV3TextToImageRequest();
        IdeogramV3TextToImageRequest.TextToImageInput input = new IdeogramV3TextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramV3TextToImageDTO.getPrompt());
        input.setRenderingSpeed(ideogramV3TextToImageDTO.getRenderingSpeed());
        input.setStyle(ideogramV3TextToImageDTO.getStyle());
        input.setExpandPrompt(ideogramV3TextToImageDTO.getExpandPrompt());
        input.setImageSize(ideogramV3TextToImageDTO.getImageSize());
        input.setNegativePrompt(ideogramV3TextToImageDTO.getNegativePrompt());
        input.setSeed(ideogramV3TextToImageDTO.getSeed());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        ImageGenerateResponse response = ideogramManager.ideogramV3TextToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram v3 text to image error: " + response.getMessage());
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

        return new BaseResponse(recordsService.create(model, ideogramV3TextToImageDTO.getPrompt(), ideogramV3TextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v3Edit(IdeogramV3EditDTO ideogramV3EditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramV3EditDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramV3EditDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramV3EditRequest request = new IdeogramV3EditRequest();
        IdeogramV3EditRequest.EditInput input = new IdeogramV3EditRequest.EditInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramV3EditDTO.getPrompt());
        input.setRenderingSpeed(ideogramV3EditDTO.getRenderingSpeed());
        input.setExpandPrompt(ideogramV3EditDTO.getExpandPrompt());
        input.setSeed(ideogramV3EditDTO.getSeed());
        input.setImageUrl(ideogramV3EditDTO.getImageUrl());
        input.setMaskUrl(ideogramV3EditDTO.getMaskUrl());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(ideogramV3EditDTO.getImageUrl());
        inputUrls.add(ideogramV3EditDTO.getMaskUrl());

        ImageGenerateResponse response = ideogramManager.ideogramV3Edit(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram v3 edit error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, ideogramV3EditDTO.getPrompt(), ideogramV3EditDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v3Remix(IdeogramV3RemixDTO ideogramV3RemixDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramV3RemixDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramV3RemixDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramV3RemixRequest request = new IdeogramV3RemixRequest();
        IdeogramV3RemixRequest.RemixInput input = new IdeogramV3RemixRequest.RemixInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramV3RemixDTO.getPrompt());
        input.setRenderingSpeed(ideogramV3RemixDTO.getRenderingSpeed());
        input.setExpandPrompt(ideogramV3RemixDTO.getExpandPrompt());
        input.setSeed(ideogramV3RemixDTO.getSeed());
        input.setImageUrl(ideogramV3RemixDTO.getImageUrl());
        input.setStyle(ideogramV3RemixDTO.getStyle());
        input.setStrength(ideogramV3RemixDTO.getStrength());
        input.setImageSize(ideogramV3RemixDTO.getImageSize());
        input.setNegativePrompt(ideogramV3RemixDTO.getNegativePrompt());
        input.setNumImages(ideogramV3RemixDTO.getNumImages());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(ideogramV3RemixDTO.getImageUrl());

        ImageGenerateResponse response = ideogramManager.ideogramV3Remix(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram v3 remix error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, ideogramV3RemixDTO.getPrompt(), ideogramV3RemixDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v3Reframe(IdeogramV3ReframeDTO ideogramV3ReframeDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramV3ReframeDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramV3ReframeDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramV3ReframeRequest request = new IdeogramV3ReframeRequest();
        IdeogramV3ReframeRequest.ReframeInput input = new IdeogramV3ReframeRequest.ReframeInput();

        request.setModel(model.getRequestName());
        input.setRenderingSpeed(ideogramV3ReframeDTO.getRenderingSpeed());
        input.setSeed(ideogramV3ReframeDTO.getSeed());
        input.setImageUrl(ideogramV3ReframeDTO.getImageUrl());
        input.setStyle(ideogramV3ReframeDTO.getStyle());
        input.setImageSize(ideogramV3ReframeDTO.getImageSize());
        input.setNumImages(ideogramV3ReframeDTO.getNumImages());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(ideogramV3ReframeDTO.getImageUrl());

        ImageGenerateResponse response = ideogramManager.ideogramV3Reframe(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram v3 reframe error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, "ideogram V3 reframe", ideogramV3ReframeDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse character(IdeogramCharacterDTO ideogramCharacterDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramCharacterDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramCharacterDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramCharacterRequest request = new IdeogramCharacterRequest();
        IdeogramCharacterRequest.CharacterInput input = new IdeogramCharacterRequest.CharacterInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramCharacterDTO.getPrompt());
        input.setRenderingSpeed(ideogramCharacterDTO.getRenderingSpeed());
        input.setStyle(ideogramCharacterDTO.getStyle());
        input.setExpandPrompt(ideogramCharacterDTO.getExpandPrompt());
        input.setImageSize(ideogramCharacterDTO.getImageSize());
        input.setNegativePrompt(ideogramCharacterDTO.getNegativePrompt());
        input.setSeed(ideogramCharacterDTO.getSeed());
        input.setNumImages(ideogramCharacterDTO.getNumImages());
        input.setReferenceImageUrls(ideogramCharacterDTO.getReferenceImageUrls());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        ImageGenerateResponse response = ideogramManager.ideogramCharacter(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram character error: " + response.getMessage());
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
                ideogramCharacterDTO.getReferenceImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, ideogramCharacterDTO.getPrompt(), ideogramCharacterDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse characterEdit(IdeogramCharacterEditDTO ideogramCharacterEditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramCharacterEditDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramCharacterEditDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramCharacterEditRequest request = new IdeogramCharacterEditRequest();
        IdeogramCharacterEditRequest.CharacterEditInput input = new IdeogramCharacterEditRequest.CharacterEditInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramCharacterEditDTO.getPrompt());
        input.setRenderingSpeed(ideogramCharacterEditDTO.getRenderingSpeed());
        input.setStyle(ideogramCharacterEditDTO.getStyle());
        input.setExpandPrompt(ideogramCharacterEditDTO.getExpandPrompt());
        input.setSeed(ideogramCharacterEditDTO.getSeed());
        input.setNumImages(ideogramCharacterEditDTO.getNumImages());
        input.setReferenceImageUrls(ideogramCharacterEditDTO.getReferenceImageUrls());
        input.setImageUrl(ideogramCharacterEditDTO.getImageUrl());
        input.setMaskUrl(ideogramCharacterEditDTO.getMaskUrl());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        ImageGenerateResponse response = ideogramManager.ideogramCharacterEdit(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram character edit error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(ideogramCharacterEditDTO.getImageUrl());
        inputUrls.add(ideogramCharacterEditDTO.getMaskUrl());

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

        return new BaseResponse(recordsService.create(model, ideogramCharacterEditDTO.getPrompt(), ideogramCharacterEditDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse characterRemix(IdeogramCharacterRemixDTO ideogramCharacterRemixDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(ideogramCharacterRemixDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(ideogramCharacterRemixDTO.getRenderingSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        IdeogramCharacterRemixRequest request = new IdeogramCharacterRemixRequest();
        IdeogramCharacterRemixRequest.CharacterRemixInput input = new IdeogramCharacterRemixRequest.CharacterRemixInput();

        request.setModel(model.getRequestName());
        input.setPrompt(ideogramCharacterRemixDTO.getPrompt());
        input.setRenderingSpeed(ideogramCharacterRemixDTO.getRenderingSpeed());
        input.setStyle(ideogramCharacterRemixDTO.getStyle());
        input.setExpandPrompt(ideogramCharacterRemixDTO.getExpandPrompt());
        input.setSeed(ideogramCharacterRemixDTO.getSeed());
        input.setNumImages(ideogramCharacterRemixDTO.getNumImages());
        input.setReferenceImageUrls(ideogramCharacterRemixDTO.getReferenceImageUrls());
        input.setImageUrl(ideogramCharacterRemixDTO.getImageUrl());
        input.setImageUrls(ideogramCharacterRemixDTO.getImageUrls());
        input.setStrength(ideogramCharacterRemixDTO.getStrength());
        input.setReferenceMaskUrls(ideogramCharacterRemixDTO.getReferenceMaskUrls());
        input.setReferenceMaskUrls(ideogramCharacterRemixDTO.getReferenceMaskUrls());
        input.setImageSize(ideogramCharacterRemixDTO.getImageSize());
        request.setCallBackUrl(callbackUrl.concat("/image/ideogram"));
        request.setInput(input);

        ImageGenerateResponse response = ideogramManager.ideogramCharacterRemix(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Ideogram character remix error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>(ideogramCharacterRemixDTO.getReferenceImageUrls());
        inputUrls.add(ideogramCharacterRemixDTO.getImageUrl());
        if(ideogramCharacterRemixDTO.getImageUrls() != null && !ideogramCharacterRemixDTO.getImageUrls().isEmpty()) {
            inputUrls.addAll(ideogramCharacterRemixDTO.getImageUrls());
        }
        if (ideogramCharacterRemixDTO.getReferenceMaskUrls() != null && !ideogramCharacterRemixDTO.getReferenceMaskUrls().isEmpty()) {
            inputUrls.add(ideogramCharacterRemixDTO.getReferenceMaskUrls());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, ideogramCharacterRemixDTO.getPrompt(), ideogramCharacterRemixDTO, userModelTask, verifyCreditsBO));
    }

}
