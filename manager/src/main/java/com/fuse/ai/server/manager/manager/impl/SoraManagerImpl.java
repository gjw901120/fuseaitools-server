package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.SoraFeignClient;
import com.fuse.ai.server.manager.manager.SoraManager;
import com.fuse.ai.server.manager.model.request.SoraGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoraManagerImpl implements SoraManager {

    @Autowired
    private SoraFeignClient soraFeignClient;

    @Override
    public VideoGenerateResponse generateVideo(SoraGenerateRequest request, String apiKey) {
        return soraFeignClient.generateVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse soraWatermarkRemover(SoraGenerateRequest request, String apiKey) {
        return soraFeignClient.soraWatermarkRemover(request, apiKey);
    }

    @Override
    public VideoGenerateResponse soraStoryboard(SoraGenerateRequest request, String apiKey) {
        return soraFeignClient.soraStoryboard(request, apiKey);
    }


}
