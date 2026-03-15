package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.MidjourneyManager;
import com.fuse.ai.server.manager.model.request.MidjourneyImagineRequest;
import com.fuse.ai.server.manager.model.request.MidjourneyUpscaleRequest;
import com.fuse.ai.server.manager.model.request.MidjourneyVaryRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.common.utils.ImageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.MidjourneyImagineDTO;
import com.fuse.ai.server.web.model.dto.request.image.MidjourneyUpscaleDTO;
import com.fuse.ai.server.web.model.dto.request.image.MidjourneyVaryDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.MidjourneyService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
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

@Service
@Slf4j
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private MidjourneyManager midjourneyManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse imagine(MidjourneyImagineDTO midjourneyImagineDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("midjourney_imagine");

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.SPEED);
        extraData.setSpeed(midjourneyImagineDTO.getSpeed());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        MidjourneyImagineRequest request = new MidjourneyImagineRequest();
        BeanUtils.copyProperties(midjourneyImagineDTO, request);
        request.setCallBackUrl(callbackUrl.concat("/image/mj-generate"));

        ImageGenerateResponse response = midjourneyManager.submitImagine(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Midjourney imagine error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        UserModelTask userModelTask = UserModelTask.create(
                userJwtDTO.getId(),
                "",
                0,
                0,
                TaskStatusEnum.PROCESSING,
                "",
                response.getData().getTaskId(),
                midjourneyImagineDTO.getFileUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, midjourneyImagineDTO.getPrompt(), midjourneyImagineDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse vary(MidjourneyVaryDTO midjourneyVaryDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName("midjourney_vary");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        MidjourneyVaryRequest request = new MidjourneyVaryRequest();
        BeanUtils.copyProperties(midjourneyVaryDTO, request);
        request.setCallBackUrl(callbackUrl.concat("/image/mj-generate"));

        ImageGenerateResponse response = midjourneyManager.submitVary(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Midjourney vary error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, "Vary image", midjourneyVaryDTO, userModelTask, verifyCreditsBO));
    }


    @Override
    public BaseResponse upscale(MidjourneyUpscaleDTO midjourneyUpscaleDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName("midjourney_upscale");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        MidjourneyUpscaleRequest request = new MidjourneyUpscaleRequest();
        BeanUtils.copyProperties(midjourneyUpscaleDTO, request);
        request.setCallBackUrl(callbackUrl.concat("/image/mj-generate"));

        ImageGenerateResponse response = midjourneyManager.submitUpscale(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Midjourney upscale error: " + response.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, "Upscale image", midjourneyUpscaleDTO, userModelTask, verifyCreditsBO));
    }

//
//    @Override
//    public BaseResponse action(MidjourneyActionDTO midjourneyActionDTO, UserJwtDTO userJwtDTO) {
//
//        Models model = modelsService.getModelByName("midjourney_action");
//
//        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());
//
//        MidjourneyActionRequest request = new MidjourneyActionRequest();
//
//        request.setChooseSameChannel(midjourneyActionDTO.getChooseSameChannel());
//        request.setCustomId(midjourneyActionDTO.getCustomId());
//        //TODO 数据库转化
//        String taskId = midjourneyActionDTO.getRecordId();
//        request.setTaskId(taskId);
//        midjourneyManager.submitAction(request, model.getRequestToken());
//
//        UserModelTask userModelTask = UserModelTask.create(
//                0,
//                "",
//                0,
//                0,
//                TaskStatusEnum.PROCESSING,
//                "",
//                "",
//                new ArrayList<>(),
//                new ArrayList<>(),
//                request,
//                new HashMap<>(),
//                new HashMap<>()
//        );
//
//        return new BaseResponse(recordsService.create(model, "midjourney_action", midjourneyActionDTO, userModelTask, verifyCreditsBO));
//    }
//
//    @Override
//    public BaseResponse blend(MidjourneyBlendDTO midjourneyBlendDTO, UserJwtDTO userJwtDTO) {
//
//        Models model = modelsService.getModelByName("midjourney_blend");
//
//        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());
//
//        MidjourneyBlendRequest request = new MidjourneyBlendRequest();
//
//        List<String> imageBast64Array = new ArrayList<>();
//
//        for (String imageUrl : midjourneyBlendDTO.getImageUrls()) {
//            imageBast64Array.add(imageUtil.getImageBase64(imageUrl));
//        }
//
//        request.setBase64Array(imageBast64Array);
//
//        request.setDimensions(MidjourneyBlendRequest.Dimensions.getByCode(midjourneyBlendDTO.getDimensions()));
//
//        midjourneyManager.submitBlend(request, model.getRequestToken());
//
//        UserModelTask userModelTask = UserModelTask.create(
//                0,
//                "",
//                0,
//                0,
//                TaskStatusEnum.PROCESSING,
//                "",
//                "",
//                midjourneyBlendDTO.getImageUrls(),
//                new ArrayList<>(),
//                request,
//                new HashMap<>(),
//                new HashMap<>()
//        );
//
//        return new BaseResponse(recordsService.create(model, "Blend multiple images", midjourneyBlendDTO, userModelTask, verifyCreditsBO));
//    }
//
//    @Override
//    public BaseResponse describe(MidjourneyDescribeDTO midjourneyDescribeDTO, UserJwtDTO userJwtDTO) {
//
//        Models model = modelsService.getModelByName("midjourney_describe");
//
//        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());
//
//        MidjourneyDescribeRequest request = new MidjourneyDescribeRequest();
//
//        List<String> inputUrls = new ArrayList<>();
//
//        inputUrls.add(midjourneyDescribeDTO.getImageUrl());
//
//        request.setBase64(imageUtil.getImageBase64(midjourneyDescribeDTO.getImageUrl()));
//
//        midjourneyManager.submitDescribe(request, model.getRequestToken());
//
//        UserModelTask userModelTask = UserModelTask.create(
//                0,
//                "",
//                0,
//                0,
//                TaskStatusEnum.PROCESSING,
//                "",
//                "",
//                inputUrls,
//                new ArrayList<>(),
//                request,
//                new HashMap<>(),
//                new HashMap<>()
//        );
//
//        return new BaseResponse(recordsService.create(model, "describe the image", midjourneyDescribeDTO, userModelTask, verifyCreditsBO));
//    }
//
//    @Override
//    public BaseResponse modal(MidjourneyModalDTO midjourneyModalDTO, UserJwtDTO userJwtDTO) {
//
//        Models model = modelsService.getModelByName("midjourney_modal");
//
//        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());
//
//        MidjourneyModalRequest request = new MidjourneyModalRequest();
//
//        request.setPrompt(midjourneyModalDTO.getPrompt());
//        List<String> inputUrls = new ArrayList<>();
//        inputUrls.add(midjourneyModalDTO.getImageUrl());
//        request.setMaskBase64(imageUtil.getImageBase64(midjourneyModalDTO.getImageUrl()));
//
//
//        //TODO 数据库转化
//        String taskId = midjourneyModalDTO.getRecordId();
//        request.setTaskId(taskId);
//
//        midjourneyManager.submitModal(request, model.getRequestToken());
//
//        UserModelTask userModelTask = UserModelTask.create(
//                0,
//                "",
//                0,
//                0,
//                TaskStatusEnum.PROCESSING,
//                "",
//                "",
//                inputUrls,
//                new ArrayList<>(),
//                request,
//                new HashMap<>(),
//                new HashMap<>()
//        );
//
//        return new BaseResponse(recordsService.create(model, midjourneyModalDTO.getPrompt(), midjourneyModalDTO, userModelTask, verifyCreditsBO));
//    }
//
//    @Override
//    public BaseResponse swapFace(MidjourneySwapFaceDTO midjourneySwapFaceDTO, UserJwtDTO userJwtDTO) {
//
//        Models model = modelsService.getModelByName("midjourney_swapface");
//
//        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());
//
//        MidjourneySwapFaceRequest request = new MidjourneySwapFaceRequest();
//        List<String> inputUrls = new ArrayList<>();
//        inputUrls.add(midjourneySwapFaceDTO.getImageUrl());
//        inputUrls.add(midjourneySwapFaceDTO.getTargetImageUrl());
//
//        request.setSourceBase64(imageUtil.getImageBase64(midjourneySwapFaceDTO.getImageUrl()));
//        request.setTargetBase64(imageUtil.getImageBase64(midjourneySwapFaceDTO.getTargetImageUrl()));
//
//        midjourneyManager.submitSwapFace(request, model.getRequestToken());
//
//        UserModelTask userModelTask = UserModelTask.create(
//                0,
//                "",
//                0,
//                0,
//                TaskStatusEnum.PROCESSING,
//                "",
//                "",
//                inputUrls,
//                new ArrayList<>(),
//                request,
//                new HashMap<>(),
//                new HashMap<>()
//        );
//
//        return new BaseResponse(recordsService.create(model, "swap face", midjourneySwapFaceDTO, userModelTask, verifyCreditsBO));
//    }

}
