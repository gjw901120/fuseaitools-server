package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.SunoResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.SunoManger;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SunoService;
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
public class SunoServiceImpl implements SunoService {

    @Autowired
    private SunoManger sunoManger;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse sunoGenerate(SunoGenerateDTO sunoGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_generate");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoGenerateRequest request = new SunoGenerateRequest();

        BeanUtils.copyProperties(sunoGenerateDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/generate"));

        SunoMusicResponse response = sunoManger.generateMusic(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoGenerateDTO.getPrompt() , userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoExtend(SunoExtendDTO sunoExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoExtendRequest request = new SunoExtendRequest();

        BeanUtils.copyProperties(sunoExtendDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/extend"));

        SunoMusicResponse response = sunoManger.extendMusic(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoExtendDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoUploadCover(SunoUploadCoverDTO sunoUploadCoverDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_upload_cover");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoUploadCoverRequest request = new SunoUploadCoverRequest();

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoUploadCoverDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/upload-cover"));

        inputUrls.add(sunoUploadCoverDTO.getFileUrl());

        request.setUploadUrl(sunoUploadCoverDTO.getFileUrl());

        SunoMusicResponse response = sunoManger.uploadCover(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoUploadCoverDTO.getPrompt() , userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoAddVocal(SunoAddVocalsDTO sunoAddVocalsDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_add_vocals");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoAddVocalsRequest request = new SunoAddVocalsRequest();

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoAddVocalsDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/add-vocals"));

        String uploadUrl = "";

        inputUrls.add(uploadUrl);

        request.setUploadUrl(uploadUrl);

        SunoMusicResponse response = sunoManger.addVocals(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoAddVocalsDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoUploadExtend(SunoUploadExtendDTO sunoUploadExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_upload_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoUploadExtendRequest request = new SunoUploadExtendRequest();

        BeanUtils.copyProperties(sunoUploadExtendDTO, request);

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(sunoUploadExtendDTO.getFileUrl());

        request.setUploadUrl(sunoUploadExtendDTO.getFileUrl());

        request.setCallBackUrl(callbackUrl.concat("/suno/upload-extend"));

        SunoMusicResponse response = sunoManger.uploadExtend(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoUploadExtendDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoAddInstrumental(SunoAddInstrumentalDTO sunoAddInstrumentalDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_add_instrumental");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoAddInstrumentalRequest request = new SunoAddInstrumentalRequest();

        BeanUtils.copyProperties(sunoAddInstrumentalDTO, request);

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(sunoAddInstrumentalDTO.getFileUrl());

        request.setUploadUrl(sunoAddInstrumentalDTO.getFileUrl());

        request.setCallBackUrl(callbackUrl.concat("/suno/add-instrumental"));

        SunoMusicResponse response = sunoManger.addInstrumental(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, sunoAddInstrumentalDTO.getTitle(), userModelTask, verifyCreditsBO));

    }


}
