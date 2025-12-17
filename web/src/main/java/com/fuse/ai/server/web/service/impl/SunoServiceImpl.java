package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.SunoResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.SunoManger;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SunoService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SunoServiceImpl implements SunoService {

    @Autowired
    private SunoManger sunoManger;

    @Autowired
    private RecordsService recordsService;

    @Override
    public BaseResponse sunoGenerate(SunoGenerateDTO sunoGenerateDTO) {

        // 实现视频生成逻辑
        SunoGenerateRequest request = new SunoGenerateRequest();

        BeanUtils.copyProperties(sunoGenerateDTO, request);

        SunoMusicResponse response = sunoManger.generateMusic(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_generate", userModelTask));

    }

    @Override
    public BaseResponse sunoExtend(SunoExtendDTO sunoExtendDTO) {

        // 实现视频生成逻辑
        SunoExtendRequest request = new SunoExtendRequest();

        BeanUtils.copyProperties(sunoExtendDTO, request);

        SunoMusicResponse response = sunoManger.extendMusic(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_extend", userModelTask));

    }

    @Override
    public BaseResponse sunoUploadCover(SunoUploadCoverDTO sunoUploadCoverDTO) {

        // 实现视频生成逻辑
        SunoUploadCoverRequest request = new SunoUploadCoverRequest();

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoUploadCoverDTO, request);

        String uploadUrl = "";

        inputUrls.add(uploadUrl);

        request.setUploadUrl(uploadUrl);

        SunoMusicResponse response = sunoManger.uploadCover(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_upload_cover", userModelTask));

    }

    @Override
    public BaseResponse sunoAddVocal(SunoAddVocalsDTO sunoAddVocalsDTO) {

        // 实现视频生成逻辑
        SunoAddVocalsRequest request = new SunoAddVocalsRequest();

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoAddVocalsDTO, request);

        String uploadUrl = "";

        inputUrls.add(uploadUrl);

        request.setUploadUrl(uploadUrl);

        SunoMusicResponse response = sunoManger.addVocals(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_add_vocals", userModelTask));

    }

    @Override
    public BaseResponse sunoUploadExtend(SunoUploadExtendDTO sunoUploadExtendDTO) {

        // 实现视频生成逻辑
        SunoUploadExtendRequest request = new SunoUploadExtendRequest();

        BeanUtils.copyProperties(sunoUploadExtendDTO, request);

        List<String> inputUrls = new ArrayList<>();

        String uploadUrl = "";

        inputUrls.add(uploadUrl);

        request.setUploadUrl(uploadUrl);

        SunoMusicResponse response = sunoManger.uploadExtend(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_upload_extend", userModelTask));

    }

    @Override
    public BaseResponse sunoAddInstrumental(SunoAddInstrumentalDTO sunoAddInstrumentalDTO) {

        // 实现视频生成逻辑
        SunoAddInstrumentalRequest request = new SunoAddInstrumentalRequest();

        BeanUtils.copyProperties(sunoAddInstrumentalDTO, request);

        List<String> inputUrls = new ArrayList<>();

        String uploadUrl = "";

        inputUrls.add(uploadUrl);

        request.setUploadUrl(uploadUrl);

        SunoMusicResponse response = sunoManger.addInstrumental(request);

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
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

        return new BaseResponse(recordsService.create("suno_add_instrumental", userModelTask));

    }


}
