package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.NanoBananaAspectRatioEnum;
import com.fuse.ai.server.manager.enums.NanoBananaOutputFormatEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.NanoBananaEditRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.NanoBananaService;
import com.fuse.ai.server.web.service.RecordsService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NanoBananaServiceImpl implements NanoBananaService {

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private RecordsService recordsService;

    @Override
    public BaseResponse nanoBananaGenerate(NanoBananaGenerateDTO nanoBananaGenerateDTO) {

        // 实现视频生成逻辑
        NanoBananaGenerateRequest request = new NanoBananaGenerateRequest();
        NanoBananaGenerateRequest.Input input = new NanoBananaGenerateRequest.Input();

        request.setModel(nanoBananaGenerateDTO.getModel());
        input.setOutputFormat(NanoBananaOutputFormatEnum.getByFormat(nanoBananaGenerateDTO.getOutputFormat()));
        input.setPrompt(nanoBananaGenerateDTO.getPrompt());
        input.setImageSize(NanoBananaAspectRatioEnum.getByRatio(nanoBananaGenerateDTO.getImageSize()));
        request.setInput(input);

        ImageGenerateResponse response = imageManager.nanoBananaGenerate(request);

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
                new ArrayList<>(),
                new ArrayList<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));

    }

    @Override
    public BaseResponse nanoBananaEdit(NanoBananaEditDTO nanoBananaEditDTO) {

        // 实现视频生成逻辑
        NanoBananaEditRequest request = new NanoBananaEditRequest();
        NanoBananaEditRequest.Input  input = new NanoBananaEditRequest.Input();

        request.setModel(nanoBananaEditDTO.getModel());
        input.setImageSize(NanoBananaAspectRatioEnum.getByRatio(nanoBananaEditDTO.getImageSize()));
        List<String> imageUrls = new ArrayList<>();
        input.setImageUrls(imageUrls);
        input.setOutputFormat(NanoBananaOutputFormatEnum.getByFormat(nanoBananaEditDTO.getOutputFormat()));
        input.setPrompt(nanoBananaEditDTO.getPrompt());
        List<String> inputUrls = new ArrayList<>();

        input.setImageUrls(inputUrls);

        request.setInput(input);

        ImageGenerateResponse response = imageManager.nanoBananaEdit(request);

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
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create("nano_banana", userModelTask));

    }

}
