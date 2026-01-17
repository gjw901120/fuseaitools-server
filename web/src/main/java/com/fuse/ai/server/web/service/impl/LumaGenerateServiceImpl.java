package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.VideoManager;
import com.fuse.ai.server.manager.model.request.LumaGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.LumaGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.LumaGenerateService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
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
public class LumaGenerateServiceImpl implements LumaGenerateService {

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
    public BaseResponse lumaGenerate(LumaGenerateDTO lumaGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("luma");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        LumaGenerateRequest request = new LumaGenerateRequest();

        BeanUtils.copyProperties(lumaGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();

        request.setVideoUrl(lumaGenerateDTO.getVideoUrl());

        request.setCallBackUrl(callbackUrl.concat("/video/luma"));

        inputUrls.add(lumaGenerateDTO.getVideoUrl());

        VideoGenerateResponse response = videoManager.lumaModify(request, model.getRequestToken());

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
                inputUrls,
                new ArrayList<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }
}
