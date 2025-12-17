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
    public VideoGenerateResponse generateVideo(SoraGenerateRequest request) {
        return soraFeignClient.generateVideo(request);
    }

    @Override
    public VideoGenerateResponse soraWatermarkRemover(SoraGenerateRequest request) {
        return soraFeignClient.soraWatermarkRemover(request);
    }

    @Override
    public VideoGenerateResponse soraStoryboard(SoraGenerateRequest request) {
        return soraFeignClient.soraStoryboard(request);
    }


}
