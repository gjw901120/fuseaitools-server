package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.VideoManager;
import com.fuse.ai.server.manager.model.request.RunwayAlephGenerateRequest;
import com.fuse.ai.server.manager.model.request.RunwayExtendRequest;
import com.fuse.ai.server.manager.model.request.RunwayGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayAlephDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.RunwayGenerateService;
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
public class RunwayGenerateServiceImpl implements RunwayGenerateService {

    @Autowired
    private VideoManager videoManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse runwayGenerate(RunwayGenerateDTO runwayGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("runway_generate");

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(runwayGenerateDTO.getDuration());
        extraData.setQuality(runwayGenerateDTO.getQuality());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        RunwayGenerateRequest request = new RunwayGenerateRequest();

        BeanUtils.copyProperties(runwayGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();

        request.setImageUrl(runwayGenerateDTO.getImageUrl());

        request.setCallBackUrl(callbackUrl.concat("/video/runway"));

        inputUrls.add(runwayGenerateDTO.getImageUrl());

        VideoGenerateResponse response = videoManager.runwayGenerate(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse runwayExtend(RunwayExtendDTO runwayExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("runway_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        RunwayExtendRequest request = new RunwayExtendRequest();

        BeanUtils.copyProperties(runwayExtendDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/video/runway"));

        VideoGenerateResponse response = videoManager.runwayExtend(request, model.getRequestToken());

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
                new ArrayList<>(),
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse runwayAleph(RunwayAlephDTO runwayAlephDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("runway_aleph");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        RunwayAlephGenerateRequest request = new RunwayAlephGenerateRequest();

        BeanUtils.copyProperties(runwayAlephDTO, request);

        VideoGenerateResponse response = videoManager.runwayAlephGenerate(request, model.getRequestToken());

        List<String> inputUrls = new ArrayList<>();

        request.setVideoUrl(runwayAlephDTO.getVideoUrl());

        request.setCallBackUrl(callbackUrl.concat("/video/runway-aleph"));

        inputUrls.add(runwayAlephDTO.getVideoUrl());
        inputUrls.add(runwayAlephDTO.getReferenceImageUrl());

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }

}
