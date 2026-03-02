package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.WanManager;
import com.fuse.ai.server.manager.model.request.WanImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanVideoToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanTextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanVideoToVideoDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.ai.server.web.service.WanService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WanServiceImpl implements WanService {

    @Autowired
    private WanManager wanManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse textToVideo(WanTextToVideoDTO wanTextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wanTextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanTextToVideoDTO.getDuration()));
        extraData.setQuality(wanTextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanTextToVideoRequest request = new WanTextToVideoRequest();

        WanTextToVideoRequest.TextToVideoInput input = new WanTextToVideoRequest.TextToVideoInput();

        input.setPrompt(wanTextToVideoDTO.getPrompt());
        input.setDuration(wanTextToVideoDTO.getDuration());
        input.setResolution(wanTextToVideoDTO.getResolution());
        input.setMultiShots(wanTextToVideoDTO.getMultiShots());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanTextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
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

        return new BaseResponse(recordsService.create(model, wanTextToVideoDTO.getPrompt(), wanTextToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToVideo(WanImageToVideoDTO wanImageToVideoDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(wanImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanImageToVideoDTO.getDuration()));
        extraData.setQuality(wanImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanImageToVideoRequest request = new WanImageToVideoRequest();

        WanImageToVideoRequest.ImageToVideoInput input = new WanImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(wanImageToVideoDTO.getPrompt());
        input.setDuration(wanImageToVideoDTO.getDuration());
        input.setResolution(wanImageToVideoDTO.getResolution());
        input.setMultiShots(wanImageToVideoDTO.getMultiShots());
        input.setImageUrls(wanImageToVideoDTO.getImageUrls());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
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
                wanImageToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wanImageToVideoDTO.getPrompt(), wanImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse videoToVideo(WanVideoToVideoDTO wanVideoToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wanVideoToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanVideoToVideoDTO.getDuration()));
        extraData.setQuality(wanVideoToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanVideoToVideoRequest request = new WanVideoToVideoRequest();

        WanVideoToVideoRequest.VideoToVideoInput input = new WanVideoToVideoRequest.VideoToVideoInput();

        input.setPrompt(wanVideoToVideoDTO.getPrompt());
        input.setDuration(wanVideoToVideoDTO.getDuration());
        input.setResolution(wanVideoToVideoDTO.getResolution());
        input.setMultiShots(wanVideoToVideoDTO.getMultiShots());
        input.setVideoUrls(wanVideoToVideoDTO.getVideoUrls());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanVideoToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
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
                wanVideoToVideoDTO.getVideoUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wanVideoToVideoDTO.getPrompt(), wanVideoToVideoDTO, userModelTask, verifyCreditsBO));
    }
}
