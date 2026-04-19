package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.KlingManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.KlingService;
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
public class KlingServiceImpl implements KlingService {

    @Autowired
    private KlingManager klingManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse turboTextToVideoPro(KlingTurboTextToVideoProDTO klingTurboTextToVideoProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(klingTurboTextToVideoProDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION);
        extraData.setDuration(Integer.valueOf(klingTurboTextToVideoProDTO.getDuration()));

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        KlingTurboTextToVideoProRequest request = new KlingTurboTextToVideoProRequest();

        KlingTurboTextToVideoProRequest.TextToVideoProInput input = new KlingTurboTextToVideoProRequest.TextToVideoProInput();

        input.setPrompt(klingTurboTextToVideoProDTO.getPrompt());
        input.setDuration(klingTurboTextToVideoProDTO.getDuration());
        input.setAspectRatio(klingTurboTextToVideoProDTO.getAspectRatio());
        input.setNegativePrompt(klingTurboTextToVideoProDTO.getNegativePrompt());
        input.setCfgScale(klingTurboTextToVideoProDTO.getCfgScale());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.klingTurboTextToVideoPro(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling turbo text to video pro error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, klingTurboTextToVideoProDTO.getPrompt(), klingTurboTextToVideoProDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse turboImageToVideoPro(KlingTurboImageToVideoProDTO klingTurboImageToVideoProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(klingTurboImageToVideoProDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION);
        extraData.setDuration(Integer.valueOf(klingTurboImageToVideoProDTO.getDuration()));

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        KlingTurboImageToVideoProRequest request = new KlingTurboImageToVideoProRequest();

        KlingTurboImageToVideoProRequest.ImageToVideoProInput input = new KlingTurboImageToVideoProRequest.ImageToVideoProInput();

        input.setPrompt(klingTurboImageToVideoProDTO.getPrompt());
        input.setImageUrl(klingTurboImageToVideoProDTO.getImageUrl());
        input.setTailImageUrl(klingTurboImageToVideoProDTO.getTailImageUrl());
        input.setDuration(klingTurboImageToVideoProDTO.getDuration());
        input.setNegativePrompt(klingTurboImageToVideoProDTO.getNegativePrompt());
        input.setCfgScale(klingTurboImageToVideoProDTO.getCfgScale());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.klingTurboImageToVideoPro(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling turbo image to video pro error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(klingTurboImageToVideoProDTO.getImageUrl());

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

        return new BaseResponse(recordsService.create(model, klingTurboImageToVideoProDTO.getPrompt(), klingTurboImageToVideoProDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse kling26TextToVideo(Kling26TextToVideoDTO kling26TextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(kling26TextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_SCENE);
        extraData.setDuration(Integer.valueOf(kling26TextToVideoDTO.getDuration()));
        extraData.setScene(kling26TextToVideoDTO.getSound() ? "with_sound" : "without_sound");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Kling26TextToVideoRequest request = new Kling26TextToVideoRequest();

        Kling26TextToVideoRequest.TextToVideo26Input input = new Kling26TextToVideoRequest.TextToVideo26Input();

        input.setPrompt(kling26TextToVideoDTO.getPrompt());
        input.setDuration(kling26TextToVideoDTO.getDuration());
        input.setAspectRatio(kling26TextToVideoDTO.getAspectRatio());
        input.setSound(kling26TextToVideoDTO.getSound());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.kling26TextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling 26 text to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, kling26TextToVideoDTO.getPrompt(), kling26TextToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse kling26ImageToVideo(Kling26ImageToVideoDTO kling26ImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(kling26ImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_SCENE);
        extraData.setDuration(Integer.valueOf(kling26ImageToVideoDTO.getDuration()));
        extraData.setScene(kling26ImageToVideoDTO.getSound() ? "with_sound" : "without_sound");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Kling26ImageToVideoRequest request = new Kling26ImageToVideoRequest();

        Kling26ImageToVideoRequest.ImageToVideo26Input input = new Kling26ImageToVideoRequest.ImageToVideo26Input();

        input.setPrompt(kling26ImageToVideoDTO.getPrompt());
        input.setImageUrls(kling26ImageToVideoDTO.getImageUrls());
        input.setDuration(kling26ImageToVideoDTO.getDuration());
        input.setSound(kling26ImageToVideoDTO.getSound());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.kling26ImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling 26 image to video error: " + response.getMsg());
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
                kling26ImageToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, kling26ImageToVideoDTO.getPrompt(), kling26ImageToVideoDTO, userModelTask, verifyCreditsBO));
    }


    @Override
    public BaseResponse kling26MotionControl(Kling26MotionControlDTO kling26MotionControlDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(kling26MotionControlDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(kling26MotionControlDTO.getDuration());
        extraData.setQuality(kling26MotionControlDTO.getMode());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Kling26MotionControlRequest request = new Kling26MotionControlRequest();

        Kling26MotionControlRequest.MotionControlInput input = new Kling26MotionControlRequest.MotionControlInput();

        input.setPrompt(kling26MotionControlDTO.getPrompt());
        input.setInputUrls(kling26MotionControlDTO.getInputUrls());
        input.setVideoUrls(kling26MotionControlDTO.getVideoUrls());
        input.setCharacterOrientation(kling26MotionControlDTO.getCharacterOrientation());
        input.setMode(kling26MotionControlDTO.getMode());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        List<String> inputUrls = kling26MotionControlDTO.getInputUrls();
        inputUrls.addAll(kling26MotionControlDTO.getVideoUrls());

        VideoGenerateResponse response = klingManager.kling26MotionControl(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling 26 motion control error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, kling26MotionControlDTO.getPrompt(), kling26MotionControlDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse aiAvatarStandard(KlingAIAvatarStandardDTO klingAIAvatarStandardDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(klingAIAvatarStandardDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        KlingAIAvatarStandardRequest request = new KlingAIAvatarStandardRequest();

        KlingAIAvatarStandardRequest.AIAvatarInput input = new KlingAIAvatarStandardRequest.AIAvatarInput();

        input.setPrompt(klingAIAvatarStandardDTO.getPrompt());
        input.setImageUrl(klingAIAvatarStandardDTO.getImageUrl());
        input.setAudioUrl(klingAIAvatarStandardDTO.getAudioUrl());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.klingAIAvatarStandard(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling AI avatar standard error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(klingAIAvatarStandardDTO.getImageUrl());
        inputUrls.add(klingAIAvatarStandardDTO.getAudioUrl());

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

        return new BaseResponse(recordsService.create(model, klingAIAvatarStandardDTO.getPrompt(), klingAIAvatarStandardDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse aiAvatarPro(KlingAIAvatarProDTO klingAIAvatarProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(klingAIAvatarProDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        KlingAIAvatarProRequest request = new KlingAIAvatarProRequest();

        KlingAIAvatarProRequest.AIAvatarProInput input = new KlingAIAvatarProRequest.AIAvatarProInput();

        input.setPrompt(klingAIAvatarProDTO.getPrompt());
        input.setImageUrl(klingAIAvatarProDTO.getImageUrl());
        input.setAudioUrl(klingAIAvatarProDTO.getAudioUrl());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.klingAIAvatarPro(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling AI avatar pro error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(klingAIAvatarProDTO.getImageUrl());
        inputUrls.add(klingAIAvatarProDTO.getAudioUrl());

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

        return new BaseResponse(recordsService.create(model, klingAIAvatarProDTO.getPrompt(), klingAIAvatarProDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse kling30Video(Kling30VideoDTO kling30VideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(kling30VideoDTO.getModel());

        // 实现视频生成逻辑
        Kling30VideoRequest request = new Kling30VideoRequest();

        Kling30VideoRequest.Video30Input input = new Kling30VideoRequest.Video30Input();

        input.setMode(kling30VideoDTO.getMode());
        input.setImageUrls(kling30VideoDTO.getImageUrls());
        input.setAspectRatio(kling30VideoDTO.getAspectRatio());
        input.setDuration(kling30VideoDTO.getDuration());
        input.setMultiShots(kling30VideoDTO.getMultiShots());
        input.setSound(kling30VideoDTO.getSound());

        List<String> inputUrls = new ArrayList<>(kling30VideoDTO.getImageUrls());

        List<Kling30VideoRequest.KlingElement> klingElements =new ArrayList<>();
        if(kling30VideoDTO.getKlingElements() != null) {
            for (Kling30VideoDTO.KlingElement klingElement : kling30VideoDTO.getKlingElements()) {
                Kling30VideoRequest.KlingElement element = new Kling30VideoRequest.KlingElement();
                element.setName(klingElement.getName());
                element.setDescription(klingElement.getDescription());
                element.setElementInputUrls(klingElement.getElementInputUrls());
                klingElements.add(element);
                inputUrls.addAll(klingElement.getElementInputUrls());
            }

        }
        input.setKlingElements(klingElements);
        int duration = 0;
        String prompt = kling30VideoDTO.getPrompt();
        if(kling30VideoDTO.getMultiShots()) {
            input.setSound(true);
            List<Kling30VideoRequest.MultiPrompt> multiPrompts = new ArrayList<>();
            for (Kling30VideoDTO.MultiPrompt multiPrompt : kling30VideoDTO.getMultiPrompt()) {
                prompt = multiPrompt.getPrompt();
                Kling30VideoRequest.MultiPrompt multi = new Kling30VideoRequest.MultiPrompt();
                //判断是否存在关联element
                if(multiPrompt.getElementName() != null && !multiPrompt.getElementName().isEmpty()) {
                    multi.setPrompt(multiPrompt.getPrompt().concat("@").concat(multiPrompt.getElementName()));
                } else {
                    multi.setPrompt(multiPrompt.getPrompt());
                }

                multi.setDuration(multiPrompt.getDuration());
                multiPrompts.add(multi);
                duration += multiPrompt.getDuration(); // Fixed: Added missing semicolon
            }
            input.setMultiPrompt(multiPrompts);
        } else {
            //判断是否存在关联element
            if(kling30VideoDTO.getKlingElements() != null && !kling30VideoDTO.getKlingElements().isEmpty() && !kling30VideoDTO.getKlingElements().get(0).getName().isEmpty()) {
                input.setPrompt(kling30VideoDTO.getPrompt().concat("@").concat(kling30VideoDTO.getKlingElements().get(0).getName()));
            } else {
                input.setPrompt(kling30VideoDTO.getPrompt());
            }
            duration = Integer.parseInt(kling30VideoDTO.getDuration());
            input.setMultiPrompt(new ArrayList<>());
        }

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_SCENE_SIZE);
        extraData.setDuration(duration);
        extraData.setSize(kling30VideoDTO.getMode());
        extraData.setScene(kling30VideoDTO.getSound() ? "with_sound" : "without_sound");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        VideoGenerateResponse response = klingManager.kling30Video(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling 30 video  error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, prompt, kling30VideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse kling30MotionControl(Kling30MotionControlDTO kling30MotionControlDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(kling30MotionControlDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_SIZE);
        extraData.setDuration(kling30MotionControlDTO.getDuration());
        extraData.setSize(kling30MotionControlDTO.getMode());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Kling30MotionControlRequest request = new Kling30MotionControlRequest();

        Kling30MotionControlRequest.Kling30MotionControlInput input = new Kling30MotionControlRequest.Kling30MotionControlInput();

        input.setPrompt(kling30MotionControlDTO.getPrompt());
        input.setInputUrls(kling30MotionControlDTO.getInputUrls());
        input.setVideoUrls(kling30MotionControlDTO.getVideoUrls());
        input.setCharacterOrientation(kling30MotionControlDTO.getCharacterOrientation());
        input.setMode(kling30MotionControlDTO.getMode());
        input.setBackgroundSource(kling30MotionControlDTO.getBackgroundSource());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/kling"));

        List<String> inputUrls = kling30MotionControlDTO.getInputUrls();
        inputUrls.addAll(kling30MotionControlDTO.getVideoUrls());
        if(kling30MotionControlDTO.getBackgroundSource() != null) {
            inputUrls.add(kling30MotionControlDTO.getBackgroundSource());
        }

        VideoGenerateResponse response = klingManager.kling30MotionControl(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Kling 30 motion control error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, kling30MotionControlDTO.getPrompt(), kling30MotionControlDTO, userModelTask, verifyCreditsBO));
    }

}