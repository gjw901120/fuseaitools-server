package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.SeedreamManager;
import com.fuse.ai.server.manager.model.request.SeedreamImageToImageRequest;
import com.fuse.ai.server.manager.model.request.SeedreamTextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.SeedreamImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.SeedreamTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.SeedreamService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SeedreamServiceImpl implements SeedreamService {

    @Autowired
    private SeedreamManager seedreamManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse textToImage(SeedreamTextToImageDTO seedreamTextToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedreamTextToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SeedreamTextToImageRequest request = new SeedreamTextToImageRequest();
        SeedreamTextToImageRequest.TextToImageInput input = new SeedreamTextToImageRequest.TextToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(seedreamTextToImageDTO.getPrompt());
        input.setQuality(seedreamTextToImageDTO.getQuality());
        input.setAspectRatio(seedreamTextToImageDTO.getAspectRatio());
        request.setCallBackUrl(callbackUrl.concat("/image/seedream"));
        request.setInput(input);

        ImageGenerateResponse response = seedreamManager.textToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedream text to image error: " + response.getMessage());
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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, seedreamTextToImageDTO.getPrompt(), seedreamTextToImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToImage(SeedreamImageToImageDTO seedreamImageToImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(seedreamImageToImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        SeedreamImageToImageRequest request = new SeedreamImageToImageRequest();
        SeedreamImageToImageRequest.ImageToImageInput input = new SeedreamImageToImageRequest.ImageToImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(seedreamImageToImageDTO.getPrompt());
        input.setQuality(seedreamImageToImageDTO.getQuality());
        input.setAspectRatio(seedreamImageToImageDTO.getAspectRatio());
        input.setImageUrls(seedreamImageToImageDTO.getImageUrls());
        request.setCallBackUrl(callbackUrl.concat("/image/seedream"));
        request.setInput(input);

        ImageGenerateResponse response = seedreamManager.imageToImage(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Seedream image to image error: " + response.getMessage());
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
                seedreamImageToImageDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, seedreamImageToImageDTO.getPrompt(), seedreamImageToImageDTO, userModelTask, verifyCreditsBO));
    }

}
