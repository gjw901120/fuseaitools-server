package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.VideoManager;
import com.fuse.ai.server.manager.model.request.RunwayAlephGenerateRequest;
import com.fuse.ai.server.manager.model.request.RunwayExtendRequest;
import com.fuse.ai.server.manager.model.request.RunwayGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.model.dto.request.video.RunwayAlephDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.RunwayGenerateService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public BaseResponse runwayGenerate(RunwayGenerateDTO runwayGenerateDTO) {
        // 实现视频生成逻辑
        RunwayGenerateRequest request = new RunwayGenerateRequest();

        BeanUtils.copyProperties(runwayGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();

        request.setImageUrl("");

        VideoGenerateResponse response = videoManager.runwayGenerate(request);

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
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("runway_generate", userModelTask));

    }

    @Override
    public BaseResponse runwayExtend(RunwayExtendDTO runwayExtendDTO) {
        // 实现视频生成逻辑
        RunwayExtendRequest request = new RunwayExtendRequest();

        BeanUtils.copyProperties(runwayExtendDTO, request);

        VideoGenerateResponse response = videoManager.runwayExtend(request);

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

        return new BaseResponse(recordsService.create("runway_extend", userModelTask));

    }

    @Override
    public BaseResponse runwayAleph(RunwayAlephDTO runwayAlephDTO) {
        // 实现视频生成逻辑
        RunwayAlephGenerateRequest request = new RunwayAlephGenerateRequest();

        BeanUtils.copyProperties(runwayAlephDTO, request);

        VideoGenerateResponse response = videoManager.runwayAlephGenerate(request);

        List<String> inputUrls = new ArrayList<>();

        request.setVideoUrl("");

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

        return new BaseResponse(recordsService.create("runway_aleph", userModelTask));

    }

}
