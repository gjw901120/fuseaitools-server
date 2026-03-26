package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.SeedreamFeignClient;
import com.fuse.ai.server.manager.manager.SeedreamManager;
import com.fuse.ai.server.manager.model.request.video.SeedreamTextToImageRequest;
import com.fuse.ai.server.manager.model.request.video.SeedreamImageToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeedreamManagerImpl implements SeedreamManager {

    @Autowired
    private SeedreamFeignClient seedreamFeignClient;

    @Override
    public ImageGenerateResponse textToImage(SeedreamTextToImageRequest request, String apiKey) {
        return seedreamFeignClient.textToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imageToImage(SeedreamImageToImageRequest request, String apiKey) {
        return seedreamFeignClient.imageToImage(request, apiKey);
    }
}