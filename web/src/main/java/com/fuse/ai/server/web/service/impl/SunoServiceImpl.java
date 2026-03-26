package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.SunoModelEnum;
import com.fuse.ai.server.manager.enums.SunoResponseCodeEnum;
import com.fuse.ai.server.manager.enums.SunoVocalGenderEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.SunoManger;
import com.fuse.ai.server.manager.model.request.audio.*;
import com.fuse.ai.server.manager.model.response.SunoMusicResponse;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
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

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoGenerateDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoGenerateDTO.getModel().getCode()));

        BeanUtils.copyProperties(sunoGenerateDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/generate"));

        SunoMusicResponse response = sunoManger.generateMusic(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno generate error: " + response.getMsg());
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
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, sunoGenerateDTO.getTitle(), sunoGenerateDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoExtend(SunoExtendDTO sunoExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoExtendRequest request = new SunoExtendRequest();

        BeanUtils.copyProperties(sunoExtendDTO, request);

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoExtendDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoExtendDTO.getModel().getCode()));

        request.setCallBackUrl(callbackUrl.concat("/suno/extend"));

        SunoMusicResponse response = sunoManger.extendMusic(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno extend error: " + response.getMsg());
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
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, sunoExtendDTO.getTitle(), sunoExtendDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoUploadCover(SunoUploadCoverDTO sunoUploadCoverDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_upload_cover");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoUploadCoverRequest request = new SunoUploadCoverRequest();

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoUploadCoverDTO, request);

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoUploadCoverDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoUploadCoverDTO.getModel().getCode()));

        request.setCallBackUrl(callbackUrl.concat("/suno/upload-cover"));

        inputUrls.add(sunoUploadCoverDTO.getFileUrl());

        request.setUploadUrl(sunoUploadCoverDTO.getFileUrl());

        SunoMusicResponse response = sunoManger.uploadCover(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno upload cover error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, sunoUploadCoverDTO.getTitle(), sunoUploadCoverDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoAddVocal(SunoAddVocalsDTO sunoAddVocalsDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_add_vocals");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoAddVocalsRequest request = new SunoAddVocalsRequest();

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoAddVocalsDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoAddVocalsDTO.getModel().getCode()));

        List<String> inputUrls = new ArrayList<>();

        BeanUtils.copyProperties(sunoAddVocalsDTO, request);

        request.setCallBackUrl(callbackUrl.concat("/suno/add-vocals"));

        inputUrls.add(sunoAddVocalsDTO.getFileUrl());

        request.setUploadUrl(sunoAddVocalsDTO.getFileUrl());

        SunoMusicResponse response = sunoManger.addVocals(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno add vocals error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, sunoAddVocalsDTO.getTitle(), sunoAddVocalsDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoUploadExtend(SunoUploadExtendDTO sunoUploadExtendDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_upload_extend");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoUploadExtendRequest request = new SunoUploadExtendRequest();

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoUploadExtendDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoUploadExtendDTO.getModel().getCode()));

        BeanUtils.copyProperties(sunoUploadExtendDTO, request);

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(sunoUploadExtendDTO.getFileUrl());

        request.setUploadUrl(sunoUploadExtendDTO.getFileUrl());

        request.setCallBackUrl(callbackUrl.concat("/suno/upload-extend"));

        SunoMusicResponse response = sunoManger.uploadExtend(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno upload extend error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, sunoUploadExtendDTO.getTitle(), sunoUploadExtendDTO, userModelTask, verifyCreditsBO));

    }

    @Override
    public BaseResponse sunoAddInstrumental(SunoAddInstrumentalDTO sunoAddInstrumentalDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("suno_add_instrumental");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SunoAddInstrumentalRequest request = new SunoAddInstrumentalRequest();

        BeanUtils.copyProperties(sunoAddInstrumentalDTO, request);

        request.setVocalGender(SunoVocalGenderEnum.fromCode(sunoAddInstrumentalDTO.getVocalGender().getCode()));
        request.setModel(SunoModelEnum.fromCode(sunoAddInstrumentalDTO.getModel().getCode()));

        List<String> inputUrls = new ArrayList<>();

        inputUrls.add(sunoAddInstrumentalDTO.getFileUrl());

        request.setUploadUrl(sunoAddInstrumentalDTO.getFileUrl());

        request.setCallBackUrl(callbackUrl.concat("/suno/add-instrumental"));

        SunoMusicResponse response = sunoManger.addInstrumental(request, model.getRequestToken());

        if(!SunoResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("suno add instrumental error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, sunoAddInstrumentalDTO.getTitle(), sunoAddInstrumentalDTO, userModelTask, verifyCreditsBO));

    }


}
