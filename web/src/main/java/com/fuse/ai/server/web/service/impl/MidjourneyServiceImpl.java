package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.MidjourneyManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.MidjourneyService;
import com.fuse.ai.server.web.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private MidjourneyManager midjourneyManager;

    @Autowired
    private RecordsService recordsService;

    @Override
    public BaseResponse imagine(MidjourneyImagineDTO midjourneyImagineDTO) {
        MidjourneyImagineRequest request = new MidjourneyImagineRequest();
        request.setPrompt(midjourneyImagineDTO.getPrompt());
        List<String> imageBast64Array = new ArrayList<>();
        List<String> inputUrls = new ArrayList<>();
        request.setBase64Array(imageBast64Array);
        midjourneyManager.submitImagine(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                inputUrls,
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

    @Override
    public BaseResponse action(MidjourneyActionDTO midjourneyActionDTO) {

        MidjourneyActionRequest request = new MidjourneyActionRequest();

        request.setChooseSameChannel(midjourneyActionDTO.getChooseSameChannel());
        request.setCustomId(midjourneyActionDTO.getCustomId());
        //TODO 数据库转化
        String taskId = midjourneyActionDTO.getRecordId();
        request.setTaskId(taskId);
        midjourneyManager.submitAction(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

    @Override
    public BaseResponse blend(MidjourneyBlendDTO midjourneyBlendDTO) {

        MidjourneyBlendRequest request = new MidjourneyBlendRequest();

        List<String> imageBast64Array = new ArrayList<>();

        List<String> inputUrls = new ArrayList<>();

        request.setBase64Array(imageBast64Array);

        request.setDimensions(MidjourneyBlendRequest.Dimensions.getByCode(midjourneyBlendDTO.getDimensions()));

        midjourneyManager.submitBlend(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                inputUrls,
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

    @Override
    public BaseResponse describe(MidjourneyDescribeDTO midjourneyDescribeDTO) {

        MidjourneyDescribeRequest request = new MidjourneyDescribeRequest();

        List<String> inputUrls = new ArrayList<>();

        try {
            request.setBase64(Base64.getEncoder().encodeToString(midjourneyDescribeDTO.getImageFile().getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        midjourneyManager.submitDescribe(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                inputUrls,
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

    @Override
    public BaseResponse modal(MidjourneyModalDTO midjourneyModalDTO) {

        MidjourneyModalRequest request = new MidjourneyModalRequest();

        request.setPrompt(midjourneyModalDTO.getPrompt());
        List<String> inputUrls = new ArrayList<>();
        try {
            request.setMaskBase64(Base64.getEncoder().encodeToString(midjourneyModalDTO.getImageFile().getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO 数据库转化
        String taskId = midjourneyModalDTO.getRecordId();
        request.setTaskId(taskId);

        midjourneyManager.submitModal(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                inputUrls,
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

    @Override
    public BaseResponse swapFace(MidjourneySwapFaceDTO midjourneySwapFaceDTO) {

        MidjourneySwapFaceRequest request = new MidjourneySwapFaceRequest();
        List<String> inputUrls = new ArrayList<>();

        try {
            request.setSourceBase64(Base64.getEncoder().encodeToString(midjourneySwapFaceDTO.getImageFile().getBytes()));
            request.setTargetBase64(Base64.getEncoder().encodeToString(midjourneySwapFaceDTO.getTargetImageFile().getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        midjourneyManager.submitSwapFace(request);

        UserModelTask userModelTask = UserModelTask.create(
                0,
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                "",
                inputUrls,
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));
    }

}
