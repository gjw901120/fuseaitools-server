package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.ImageFeignClient;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageManagerImpl implements ImageManager {

    @Autowired
    private ImageFeignClient imageFeignClient;


    /**
     * 生成GPT-4o图像
     */
    @Override
    public ImageGenerateResponse gpt4oImageGenerate(Gpt4oImageGenerateRequest request, String apiKey) {
        return imageFeignClient.gpt4oImageGenerate(request, apiKey);
    }

    /**
     * 生成/编辑flux-kontext图像
     */
    @Override
    public ImageGenerateResponse fluxKontextGenerate(FluxKontextImageRequest request, String apiKey) {
        return imageFeignClient.fluxKontextGenerate(request, apiKey);
    }

    /**
     * 生成图像
     */
    @Override
    public ImageGenerateResponse nanoBananaGenerate(NanoBananaGenerateRequest request, String apiKey) {
        return imageFeignClient.nanoBananaGenerate(request, apiKey);
    }

    /**
     * 编辑图像
     */
    @Override
    public ImageGenerateResponse nanoBananaEdit(NanoBananaEditRequest request, String apiKey) {
        return imageFeignClient.nanoBananaEdit(request, apiKey);
    }

    @Override
    public ImageGenerateResponse nanoBananaProGenerate(NanoBananaProGenerateRequest request, String apiKey) {
        return imageFeignClient.nanoBananaProGenerate(request, apiKey);
    }

}
