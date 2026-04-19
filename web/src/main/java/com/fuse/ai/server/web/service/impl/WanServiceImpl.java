package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.manager.WanManager;
import com.fuse.ai.server.manager.model.request.image.Wan27ImageProRequest;
import com.fuse.ai.server.manager.model.request.image.Wan27ImageRequest;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.dto.request.image.Wan27ImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.Wan27ImageProDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.ai.server.web.service.WanService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WanServiceImpl implements WanService {

    @Autowired
    private WanManager wanManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private ImageManager imageManager;

    @Value("${callback.url}")
    private String callbackUrl;


    @Override
    public BaseResponse textToVideo(WanTextToVideoDTO wanTextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wanTextToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanTextToVideoDTO.getDuration()));
        extraData.setQuality(wanTextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanTextToVideoRequest request = new WanTextToVideoRequest();

        WanTextToVideoRequest.TextToVideoInput input = new WanTextToVideoRequest.TextToVideoInput();

        input.setPrompt(wanTextToVideoDTO.getPrompt());
        input.setDuration(wanTextToVideoDTO.getDuration());
        input.setResolution(wanTextToVideoDTO.getResolution());
        input.setMultiShots(wanTextToVideoDTO.getMultiShots());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanTextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan text to video error: " + response.getMsg());
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

        return new BaseResponse(recordsService.create(model, wanTextToVideoDTO.getPrompt(), wanTextToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse imageToVideo(WanImageToVideoDTO wanImageToVideoDTO, UserJwtDTO userJwtDTO) {

        Models model = modelsService.getModelByName(wanImageToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanImageToVideoDTO.getDuration()));
        extraData.setQuality(wanImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanImageToVideoRequest request = new WanImageToVideoRequest();

        WanImageToVideoRequest.ImageToVideoInput input = new WanImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(wanImageToVideoDTO.getPrompt());
        input.setDuration(wanImageToVideoDTO.getDuration());
        input.setResolution(wanImageToVideoDTO.getResolution());
        input.setMultiShots(wanImageToVideoDTO.getMultiShots());
        input.setImageUrls(wanImageToVideoDTO.getImageUrls());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan image to video error: " + response.getMsg());
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
                wanImageToVideoDTO.getImageUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wanImageToVideoDTO.getPrompt(), wanImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse videoToVideo(WanVideoToVideoDTO wanVideoToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wanVideoToVideoDTO.getModel());

        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wanVideoToVideoDTO.getDuration()));
        extraData.setQuality(wanVideoToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        WanVideoToVideoRequest request = new WanVideoToVideoRequest();

        WanVideoToVideoRequest.VideoToVideoInput input = new WanVideoToVideoRequest.VideoToVideoInput();

        input.setPrompt(wanVideoToVideoDTO.getPrompt());
        input.setDuration(wanVideoToVideoDTO.getDuration());
        input.setResolution(wanVideoToVideoDTO.getResolution());
        input.setMultiShots(wanVideoToVideoDTO.getMultiShots());
        input.setVideoUrls(wanVideoToVideoDTO.getVideoUrls());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wanVideoToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan video to video error: " + response.getMsg());
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
                wanVideoToVideoDTO.getVideoUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                response,
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wanVideoToVideoDTO.getPrompt(), wanVideoToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27TextToVideo(Wan27TextToVideoDTO wan27TextToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27TextToVideoDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wan27TextToVideoDTO.getDuration()));
        extraData.setQuality(wan27TextToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Wan27TextToVideoRequest request = new Wan27TextToVideoRequest();

        Wan27TextToVideoRequest.TextToVideoInput input = new Wan27TextToVideoRequest.TextToVideoInput();

        input.setPrompt(wan27TextToVideoDTO.getPrompt());
        input.setDuration(Integer.valueOf(wan27TextToVideoDTO.getDuration()));
        input.setResolution(wan27TextToVideoDTO.getResolution());
        input.setAudioUrl(wan27TextToVideoDTO.getAudioUrl());
        input.setPromptExtend(wan27TextToVideoDTO.getPromptExtend());
        input.setNsfwChecker(wan27TextToVideoDTO.getNsfwChecker());
        input.setWatermark(wan27TextToVideoDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27TextToVideoDTO.getSeed()));
        input.setNegativePrompt(wan27TextToVideoDTO.getNegativePrompt());
        input.setRatio(wan27TextToVideoDTO.getRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wan27TextToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan 27 text to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(wan27TextToVideoDTO.getAudioUrl());
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

        return new BaseResponse(recordsService.create(model, wan27TextToVideoDTO.getPrompt(), wan27TextToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27ImageToVideo(Wan27ImageToVideoDTO wan27ImageToVideoDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27ImageToVideoDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wan27ImageToVideoDTO.getDuration()));
        extraData.setQuality(wan27ImageToVideoDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Wan27ImageToVideoRequest request = new Wan27ImageToVideoRequest();

        Wan27ImageToVideoRequest.ImageToVideoInput input = new Wan27ImageToVideoRequest.ImageToVideoInput();

        input.setPrompt(wan27ImageToVideoDTO.getPrompt());
        input.setDuration(Integer.valueOf(wan27ImageToVideoDTO.getDuration()));
        input.setResolution(wan27ImageToVideoDTO.getResolution());
        input.setFirstClipUrl(wan27ImageToVideoDTO.getFirstClipUrl());
        input.setFirstFrameUrl(wan27ImageToVideoDTO.getFirstFrameUrl());
        input.setLastFrameUrl(wan27ImageToVideoDTO.getLastFrameUrl());
        input.setDrivingAudioUrl(wan27ImageToVideoDTO.getDrivingAudioUrl());
        input.setPromptExtend(wan27ImageToVideoDTO.getPromptExtend());
        input.setNsfwChecker(wan27ImageToVideoDTO.getNsfwChecker());
        input.setWatermark(wan27ImageToVideoDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27ImageToVideoDTO.getSeed()));
        input.setNegativePrompt(wan27ImageToVideoDTO.getNegativePrompt());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wan27ImageToVideo(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan 27 image to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(wan27ImageToVideoDTO.getFirstFrameUrl());
        inputUrls.add(wan27ImageToVideoDTO.getLastFrameUrl());
        inputUrls.add(wan27ImageToVideoDTO.getFirstClipUrl());
        inputUrls.add(wan27ImageToVideoDTO.getDrivingAudioUrl());
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

        return new BaseResponse(recordsService.create(model, wan27ImageToVideoDTO.getPrompt(), wan27ImageToVideoDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27VideoEdit(Wan27VideoEditDTO wan27VideoEditDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27VideoEditDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wan27VideoEditDTO.getDuration()));
        extraData.setQuality(wan27VideoEditDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Wan27VideoEditRequest request = new Wan27VideoEditRequest();

        Wan27VideoEditRequest.VideoEditInput input = new Wan27VideoEditRequest.VideoEditInput();

        input.setPrompt(wan27VideoEditDTO.getPrompt());
        input.setDuration(Integer.valueOf(wan27VideoEditDTO.getDuration()));
        input.setResolution(wan27VideoEditDTO.getResolution());
        input.setVideoUrl(wan27VideoEditDTO.getVideoUrl());
        input.setPromptExtend(wan27VideoEditDTO.getPromptExtend());
        input.setNsfwChecker(wan27VideoEditDTO.getNsfwChecker());
        input.setWatermark(wan27VideoEditDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27VideoEditDTO.getSeed()));
        input.setNegativePrompt(wan27VideoEditDTO.getNegativePrompt());
        input.setAudioSetting(wan27VideoEditDTO.getAudioSetting());
        input.setReferenceImage(wan27VideoEditDTO.getReferenceImage());
        input.setAspectRatio(wan27VideoEditDTO.getAspectRatio());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wan27VideoEdit(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan 27 video edit error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.add(wan27VideoEditDTO.getReferenceImage());
        inputUrls.add(wan27VideoEditDTO.getVideoUrl());
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

        return new BaseResponse(recordsService.create(model, wan27VideoEditDTO.getPrompt(), wan27VideoEditDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27R2V(Wan27R2vDTO wan27R2vDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27R2vDTO.getModel());
        ExtraDataBO extraData = new ExtraDataBO();
        extraData.setType(ExtraDataEnum.PER_DURATION_QUALITY);
        extraData.setDuration(Integer.valueOf(wan27R2vDTO.getDuration()));
        extraData.setQuality(wan27R2vDTO.getResolution());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, extraData);

        // 实现视频生成逻辑
        Wan27R2VRequest request = new Wan27R2VRequest();

        Wan27R2VRequest.R2VInput input = new Wan27R2VRequest.R2VInput();

        input.setPrompt(wan27R2vDTO.getPrompt());
        input.setDuration(Integer.valueOf(wan27R2vDTO.getDuration()));
        input.setResolution(wan27R2vDTO.getResolution());
        input.setPromptExtend(wan27R2vDTO.getPromptExtend());
        input.setNsfwChecker(wan27R2vDTO.getNsfwChecker());
        input.setWatermark(wan27R2vDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27R2vDTO.getSeed()));
        input.setNegativePrompt(wan27R2vDTO.getNegativePrompt());
        input.setFirstFrame(wan27R2vDTO.getFirstFrame());
        input.setReferenceImage(wan27R2vDTO.getReferenceImage());
        input.setAspectRatio(wan27R2vDTO.getAspectRatio());
        input.setReferenceVoice(wan27R2vDTO.getReferenceVoice());
        input.setReferenceVideo(wan27R2vDTO.getReferenceVideo());

        request.setInput(input);
        request.setModel(model.getRequestName());

        request.setCallBackUrl(callbackUrl.concat("/video/wan"));

        VideoGenerateResponse response = wanManager.wan27R2V(request, model.getRequestToken());

        if(!ResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("Wan 27 r to video error: " + response.getMsg());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "The volume of service requests is too high. Please try again later.");
        }

        List<String> inputUrls = new ArrayList<>();
        inputUrls.addAll(wan27R2vDTO.getReferenceVideo());
        inputUrls.addAll(wan27R2vDTO.getReferenceImage());
        inputUrls.add(wan27R2vDTO.getReferenceVoice());
        inputUrls.add(wan27R2vDTO.getFirstFrame());
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

        return new BaseResponse(recordsService.create(model, wan27R2vDTO.getPrompt(), wan27R2vDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27Image(Wan27ImageDTO wan27ImageDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27ImageDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        Wan27ImageRequest request = new Wan27ImageRequest();
        Wan27ImageRequest.Wan27ImageInput input = new Wan27ImageRequest.Wan27ImageInput();

        request.setModel(model.getRequestName());
        input.setPrompt(wan27ImageDTO.getPrompt());
        input.setAspectRatio(wan27ImageDTO.getAspectRatio());
        input.setEnableSequential(wan27ImageDTO.getEnableSequential());
        input.setN(Integer.valueOf(wan27ImageDTO.getN()));
        input.setResolution(wan27ImageDTO.getResolution());
        input.setThinkingMode(wan27ImageDTO.getThinkingMode());
        input.setBboxList(wan27ImageDTO.getBboxList());
        input.setWatermark(wan27ImageDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27ImageDTO.getSeed()));
        input.setNsfwChecker(wan27ImageDTO.getNsfwChecker());
        input.setInputUrls(wan27ImageDTO.getInputUrls());
        if(wan27ImageDTO.getColorPalette() != null && wan27ImageDTO.getColorPalette().size() > 0) {
            List<Wan27ImageRequest.ColorPaletteItem> convertedList =
                    wan27ImageDTO.getColorPalette().stream()
                            .map(item -> {
                                Wan27ImageRequest.ColorPaletteItem newItem = new Wan27ImageRequest.ColorPaletteItem();
                                newItem.setHex(item.getHex());
                                newItem.setRatio(item.getRatio());
                                return newItem;
                            })
                            .collect(Collectors.toList());
            input.setColorPalette(convertedList);
        }
        request.setCallBackUrl(callbackUrl.concat("/image/wan"));
        request.setInput(input);

        ImageGenerateResponse response = imageManager.wan27Image(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("wan 27 image error: " + response.getMessage());
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
                wan27ImageDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wan27ImageDTO.getPrompt(), wan27ImageDTO, userModelTask, verifyCreditsBO));
    }

    @Override
    public BaseResponse v27ImagePro(Wan27ImageProDTO wan27ImageProDTO, UserJwtDTO userJwtDTO) {
        Models model = modelsService.getModelByName(wan27ImageProDTO.getModel());

        verifyCreditsBO verifyCreditsBO = userCreditsService.verifyCredits(userJwtDTO.getId(), model, new ExtraDataBO());

        // 实现视频生成逻辑
        Wan27ImageProRequest request = new Wan27ImageProRequest();
        Wan27ImageProRequest.Wan27ImageProInput input = new Wan27ImageProRequest.Wan27ImageProInput();

        request.setModel(model.getRequestName());
        input.setPrompt(wan27ImageProDTO.getPrompt());
        input.setAspectRatio(wan27ImageProDTO.getAspectRatio());
        input.setEnableSequential(wan27ImageProDTO.getEnableSequential());
        input.setN(Integer.valueOf(wan27ImageProDTO.getN()));
        input.setResolution(wan27ImageProDTO.getResolution());
        input.setThinkingMode(wan27ImageProDTO.getThinkingMode());
        input.setBboxList(wan27ImageProDTO.getBboxList());
        input.setWatermark(wan27ImageProDTO.getWatermark());
        input.setSeed(Long.valueOf(wan27ImageProDTO.getSeed()));
        input.setNsfwChecker(wan27ImageProDTO.getNsfwChecker());
        input.setInputUrls(wan27ImageProDTO.getInputUrls());
        if(wan27ImageProDTO.getColorPalette() != null && wan27ImageProDTO.getColorPalette().size() > 0) {
            List<Wan27ImageProRequest.ColorPaletteItem> convertedList =
                    wan27ImageProDTO.getColorPalette().stream()
                            .map(item -> {
                                Wan27ImageProRequest.ColorPaletteItem newItem = new Wan27ImageProRequest.ColorPaletteItem();
                                newItem.setHex(item.getHex());
                                newItem.setRatio(item.getRatio());
                                return newItem;
                            })
                            .collect(Collectors.toList());
            input.setColorPalette(convertedList);
        }
        request.setCallBackUrl(callbackUrl.concat("/image/wan"));
        request.setInput(input);

        ImageGenerateResponse response = imageManager.wan27ImagePro(request, model.getRequestToken());

        if(!ImageResponseCodeEnum.SUCCESS.equals(response.getCode())) {
            FeishuMessageUtil.sendExceptionMessage("wan 27 image pro error: " + response.getMessage());
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
                wan27ImageProDTO.getInputUrls(),
                new ArrayList<>(),
                new HashMap<>(),
                request,
                new HashMap<>(),
                new HashMap<>()
        );

        return new BaseResponse(recordsService.create(model, wan27ImageProDTO.getPrompt(), wan27ImageProDTO, userModelTask, verifyCreditsBO));
    }
}
