package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.SeedanceFeignClient;
import com.fuse.ai.server.manager.manager.SeedanceManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeedanceManagerImpl implements SeedanceManager {

    @Autowired
    private SeedanceFeignClient seedanceFeignClient;

    @Override
    public VideoGenerateResponse liteTextToVideo(SeedanceLiteTextToVideoRequest request, String apiKey) {
        return seedanceFeignClient.liteTextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse liteImageToVideo(SeedanceLiteImageToVideoRequest request, String apiKey) {
        return seedanceFeignClient.liteImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse proTextToVideo(SeedanceProTextToVideoRequest request, String apiKey) {
        return seedanceFeignClient.proTextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse proImageToVideo(SeedanceProImageToVideoRequest request, String apiKey) {
        return seedanceFeignClient.proImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse proFastImageToVideo(SeedanceProFastImageToVideoRequest request, String apiKey) {
        return seedanceFeignClient.proFastImageToVideo(request, apiKey);
    }
}