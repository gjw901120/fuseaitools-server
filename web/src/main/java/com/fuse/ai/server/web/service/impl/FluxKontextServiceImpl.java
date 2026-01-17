package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.FluxKontextImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.FluxKontextGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.FluxKontextService;
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
public class FluxKontextServiceImpl implements FluxKontextService {

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
    public BaseResponse fluxKontextGenerate(FluxKontextGenerateDTO fluxKontextGenerateDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(fluxKontextGenerateDTO.getModel().getCode());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        FluxKontextImageRequest request = new FluxKontextImageRequest();

        BeanUtils.copyProperties(fluxKontextGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();

        request.setInputImage(fluxKontextGenerateDTO.getImageUrl());
        inputUrls.add(fluxKontextGenerateDTO.getImageUrl());

        request.setCallBackUrl(callbackUrl.concat("/image/flux-kontext"));

        ImageGenerateResponse response = imageManager.fluxKontextGenerate(request, model.getRequestToken());

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

        return new BaseResponse(recordsService.create(model, request.getPrompt(), userModelTask, verifyCreditsBO));

    }
}
