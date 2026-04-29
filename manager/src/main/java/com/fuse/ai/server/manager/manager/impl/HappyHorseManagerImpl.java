package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.HappyHorseFeignClient;
import com.fuse.ai.server.manager.manager.HappyHorseManager;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ReferenceToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1TextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1VideoEditRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HappyHorseManagerImpl implements HappyHorseManager {

    @Autowired
    private HappyHorseFeignClient happyHorseFeignClient;

    @Override
    public VideoGenerateResponse v1TextToVideo(HappyHorseV1TextToVideoRequest request, String apiKey) {
        return happyHorseFeignClient.v1TextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse v1ImageToVideo(HappyHorseV1ImageToVideoRequest request, String apiKey) {
        return happyHorseFeignClient.v1ImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse v1ReferenceToVideo(HappyHorseV1ReferenceToVideoRequest request, String apiKey) {
        return happyHorseFeignClient.v1ReferenceToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse v1VideoEdit(HappyHorseV1VideoEditRequest request, String apiKey) {
        return happyHorseFeignClient.v1VideoEdit(request, apiKey);
    }
}
