package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.Gpt4oImageGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.Gpt4oImageGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.Gpt4oImageService;
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
public class Gpt4oImageServiceImpl implements Gpt4oImageService {

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Value("${callback.url}")
    private String callbackUrl;

    @Override
    public BaseResponse gpt4oImageGenerate(Gpt4oImageGenerateDTO gpt4oImageGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName("GPT_4o_image");

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();

        BeanUtils.copyProperties(gpt4oImageGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>(gpt4oImageGenerateDTO.getImageUrls());

        request.setMaskUrl(gpt4oImageGenerateDTO.getMaskUrl());
        request.setFilesUrl(gpt4oImageGenerateDTO.getImageUrls());
        request.setCallBackUrl(callbackUrl.concat("/image/gpt4o-image"));
        inputUrls.add(gpt4oImageGenerateDTO.getMaskUrl());

        ImageGenerateResponse response = imageManager.gpt4oImageGenerate(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, response.getMessage());
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

        return new BaseResponse(recordsService.create(model, gpt4oImageGenerateDTO.getPrompt(), userModelTask, verifyCreditsBO));

    }
}
