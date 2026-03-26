package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.video.SeedreamImageToImageRequest;
import com.fuse.ai.server.manager.model.request.video.SeedreamTextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;

public interface SeedreamManager {

    /**
     * Seedream 文生图
     */
    ImageGenerateResponse textToImage(SeedreamTextToImageRequest request, String apiKey);

    /**
     * Seedream 图生图
     */
    ImageGenerateResponse imageToImage(SeedreamImageToImageRequest request, String apiKey);
}