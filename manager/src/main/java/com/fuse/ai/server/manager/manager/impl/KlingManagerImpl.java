package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.KlingFeignClient;
import com.fuse.ai.server.manager.manager.KlingManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KlingManagerImpl implements KlingManager {

    @Autowired
    private KlingFeignClient klingFeignClient;

    @Override
    public VideoGenerateResponse klingTurboTextToVideoPro(KlingTurboTextToVideoProRequest request, String apiKey) {
        return klingFeignClient.klingTurboTextToVideoPro(request, apiKey);
    }

    @Override
    public VideoGenerateResponse klingTurboImageToVideoPro(KlingTurboImageToVideoProRequest request, String apiKey) {
        return klingFeignClient.klingTurboImageToVideoPro(request, apiKey);
    }

    @Override
    public VideoGenerateResponse kling26TextToVideo(Kling26TextToVideoRequest request, String apiKey) {
        return klingFeignClient.kling26TextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse kling26ImageToVideo(Kling26ImageToVideoRequest request, String apiKey) {
        return klingFeignClient.kling26ImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse kling26MotionControl(Kling26MotionControlRequest request, String apiKey) {
        return klingFeignClient.kling26MotionControl(request, apiKey);
    }

    @Override
    public VideoGenerateResponse klingAIAvatarStandard(KlingAIAvatarStandardRequest request, String apiKey) {
        return klingFeignClient.klingAIAvatarStandard(request, apiKey);
    }

    @Override
    public VideoGenerateResponse klingAIAvatarPro(KlingAIAvatarProRequest request, String apiKey) {
        return klingFeignClient.klingAIAvatarPro(request, apiKey);
    }

    @Override
    public VideoGenerateResponse kling30Video(Kling30VideoRequest request, String apiKey) {
        return klingFeignClient.kling30Video(request, apiKey);
    }

    @Override
    public VideoGenerateResponse kling30MotionControl(Kling30MotionControlRequest request, String apiKey) {
        return klingFeignClient.kling30MotionControl(request, apiKey);
    }
}