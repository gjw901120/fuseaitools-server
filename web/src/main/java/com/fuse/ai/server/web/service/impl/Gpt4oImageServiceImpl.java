package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.Gpt4oImageGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.web.model.dto.request.image.Gpt4oImageGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.Gpt4oImageService;
import com.fuse.ai.server.web.service.RecordsService;
import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public BaseResponse gpt4oImageGenerate(Gpt4oImageGenerateDTO gpt4oImageGenerateDTO) {

        // 实现视频生成逻辑
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();

        BeanUtils.copyProperties(gpt4oImageGenerateDTO, request);

        List<String> inputUrls = new ArrayList<>();
        String maskUrl = "";

        request.setMaskUrl(maskUrl);
        request.setFilesUrl(inputUrls);
        inputUrls.add(maskUrl);

        ImageGenerateResponse response = imageManager.gpt4oImageGenerate(request);

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

        return new BaseResponse(recordsService.create("GPT_4o_image", userModelTask));

    }
}
