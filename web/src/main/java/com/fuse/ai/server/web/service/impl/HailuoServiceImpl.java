package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.HailuoManager;
import com.fuse.ai.server.manager.model.request.video.HailuoImageToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoProDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoStandardDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.HailuoService;
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
public class HailuoServiceImpl implements HailuoService {

    @Autowired
    private HailuoManager hailuoManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse imageToVideoStandard(HailuoImageToVideoStandardDTO hailuoImageToVideoStandardDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(hailuoImageToVideoStandardDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(hailuoImageToVideoStandardDTO.getDuration()));
        extraData.setQuality(hailuoImageToVideoStandardDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HailuoImageToVideoRequest request = new HailuoImageToVideoRequest();

        HailuoImageToVideoRequest.ImageToVideoInput input = new HailuoImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(hailuoImageToVideoStandardDTO.getPrompt());
        input.setDuration(hailuoImageToVideoStandardDTO.getDuration());
        input.setResolution(hailuoImageToVideoStandardDTO.getResolution());
        input.setImageUrl(hailuoImageToVideoStandardDTO.getImageUrl());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/hailuo"));

        VideoGenerateResponse response = hailuoManager.hailuoImageToVideo(request, model.getRequestToken());

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(hailuoImageToVideoStandardDTO.getImageUrl());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Hailuo image to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, hailuoImageToVideoStandardDTO.getPrompt(), hailuoImageToVideoStandardDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToVideoPro(HailuoImageToVideoProDTO hailuoImageToVideoProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(hailuoImageToVideoProDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(hailuoImageToVideoProDTO.getDuration()));
        extraData.setQuality(hailuoImageToVideoProDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        HailuoImageToVideoRequest request = new HailuoImageToVideoRequest();

        HailuoImageToVideoRequest.ImageToVideoInput input = new HailuoImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(hailuoImageToVideoProDTO.getPrompt());
        input.setDuration(hailuoImageToVideoProDTO.getDuration());
        input.setResolution(hailuoImageToVideoProDTO.getResolution());
        input.setImageUrl(hailuoImageToVideoProDTO.getImageUrl());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/hailuo"));

        VideoGenerateResponse response = hailuoManager.hailuoImageToVideo(request, model.getRequestToken());

        ArrayList<String> inputUrls = new ArrayList<>();
        inputUrls.add(hailuoImageToVideoProDTO.getImageUrl());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Hailuo image to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, hailuoImageToVideoProDTO.getPrompt(), hailuoImageToVideoProDTO, userModelTask, verifyCreditsBO));
    }
}