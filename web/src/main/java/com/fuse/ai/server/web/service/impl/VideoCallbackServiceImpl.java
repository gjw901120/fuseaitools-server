package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.web.common.enums.VideoRequestCodeEnum;
import com.fuse.ai.server.web.model.dto.request.callback.video.*;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.VideoCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频回调服务实现
 */
@Service
@Slf4j
public class VideoCallbackServiceImpl implements VideoCallbackService {

    @Autowired
    private RecordsService recordsService;


    @Override
    public void VeoCallback(VeoCallbackRequest request) {
        VeoCallbackData data = request.getData();
        log.info("Veo回调处理完成, taskId: {} , response: {}", data.getTaskId(), request);

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(data.getTaskId(), outputUrl, request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void RunwayCallback(RunwayCallbackRequest request) {
        RunwayCallbackData data = request.getData();
        log.info("Runway回调处理完成, taskId: {}", data.getTaskId());

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(data.getTaskId(), outputUrl, request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void RunwayAlephCallback(RunwayAlephCallbackRequest request) {
        RunwayAlephCallbackData data = request.getData();
        log.info("RunwayAleph回调处理完成, taskId: {}", request.getTaskId());

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(request.getTaskId(), outputUrl, request);
        } else {
            recordsService.failed(request.getTaskId(), request);
        }

    }

    @Override
    public void LumaCallback(LumaCallbackRequest request) {
        LumaCallbackData data = request.getData();
        log.info("Luma回调处理完成, taskId: {}", data.getTaskId());

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(data.getTaskId(), outputUrl, request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void SoraCallback(SoraCallbackRequest request) {
        SoraCallbackData data = request.getData();
        log.info("Sora回调处理完成, taskId: {}", data.getTaskId());

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            recordsService.completed(data.getTaskId(), outputUrl, request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

}