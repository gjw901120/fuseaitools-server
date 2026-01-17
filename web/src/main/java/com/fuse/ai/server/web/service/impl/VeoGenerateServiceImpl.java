package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.VideoManager;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.manager.model.request.VeoGenerateRequest;
import com.fuse.ai.server.manager.model.request.VeoExtendRequest;
import com.fuse.ai.server.manager.enums.VeoModelEnum;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.VeoExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.VeoGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.ai.server.web.service.VeoGenerateService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Veo视频生成服务实现
 */
@Service
public class VeoGenerateServiceImpl implements VeoGenerateService {

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
    public BaseResponse generateVideo(VeoGenerateDTO veoGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(veoGenerateDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        VeoGenerateRequest request = new VeoGenerateRequest();

        BeanUtils.copyProperties(veoGenerateDTO, request);

        request.setImageUrls(veoGenerateDTO.getImageUrls());

        request.setModel(VeoModelEnum.getByCode(model.getRequestName()));

        request.setCallBackUrl(callbackUrl.concat("video/veo"));

        VideoGenerateResponse response = videoManager.veoGenerate(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
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
                veoGenerateDTO.getImageUrls(),
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse extendVideo(VeoExtendDTO veoExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("veo3_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        VeoExtendRequest request =  new VeoExtendRequest();

        BeanUtils.copyProperties(veoExtendDTO, request);

        request.setCallBackUrl(callbackUrl.concat("video/veo"));

        VideoGenerateResponse response = videoManager.veoExtend(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMsg());
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
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }

}