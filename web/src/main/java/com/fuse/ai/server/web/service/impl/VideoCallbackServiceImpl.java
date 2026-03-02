package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.web.common.enums.VideoRequestCodeEnum;
import com.fuse.ai.server.web.common.utils.S3UploadUtil;
import com.fuse.ai.server.web.model.dto.request.callback.video.*;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.VideoCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 视频回调服务实现
 */
@Service
@Slf4j
public class VideoCallbackServiceImpl implements VideoCallbackService {

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private S3UploadUtil s3UploadUtil;


    @Override
    public void VeoCallback(VeoCallbackRequest request) {
        VeoCallbackData data = request.getData();

        //幂等性校验
        if (recordsService.isCompleted(data.getTaskId())) return;

        log.info("Veo回调处理完成, taskId: {} , response: {}", data.getTaskId(), request);

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getInfo().getResultUrls().get(0)));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void RunwayCallback(RunwayCallbackRequest request) {
        RunwayCallbackData data = request.getData();
        log.info("Runway回调处理完成, taskId: {}", data.getTaskId());

        if (recordsService.isCompleted(data.getTaskId())) return;

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getImageUrl()));
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getVideoUrl()));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void RunwayAlephCallback(RunwayAlephCallbackRequest request) {
        RunwayAlephCallbackData data = request.getData();
        log.info("RunwayAleph回调处理完成, taskId: {}", request.getTaskId());

        if (recordsService.isCompleted(request.getTaskId())) return;
        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultVideoUrl()));
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultImageUrl()));
            recordsService.completed(request.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(request.getTaskId(), request);
        }

    }

    @Override
    public void LumaCallback(LumaCallbackRequest request) {
        LumaCallbackData data = request.getData();
        log.info("Luma回调处理完成, taskId: {}", data.getTaskId());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultUrls().get(0)));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void SoraCallback(SoraCallbackRequest request) {
        SoraCallbackData data = request.getData();
        log.info("Sora回调处理完成, taskId: {}", data.getTaskId());

        //幂等性校验
        if (recordsService.isCompleted(request.getData().getTaskId())) return;

        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultUrls().get(0)));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }

    }

    @Override
    public void SeedanceCallback(SeedanceCallbackRequest request) {
        SeedanceCallbackData data = request.getData();
        log.info("Seedance回调处理完成, taskId: {}", data.getTaskId());
        if (recordsService.isCompleted(data.getTaskId())) return;
        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultUrls().get(0)));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }
    }

    @Override
    public void WanCallback(WanCallbackRequest request) {
        WanCallbackData data = request.getData();
        log.info("Wan回调处理完成, taskId: {}", data.getTaskId());
        if (recordsService.isCompleted(data.getTaskId())) return;
        if(request.getCode().equals(VideoRequestCodeEnum.SUCCESS.getCode())) {
            List<String> outputUrl = new ArrayList<>();
            outputUrl.add(s3UploadUtil.uploadFileFromUrl(data.getResultUrls().get(0)));
            recordsService.completed(data.getTaskId(), outputUrl, new HashMap<>(), request);
        } else {
            recordsService.failed(data.getTaskId(), request);
        }
    }

}