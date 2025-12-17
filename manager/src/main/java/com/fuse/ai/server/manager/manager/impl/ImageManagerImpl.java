package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.ImageFeignClient;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.FluxKontextImageRequest;
import com.fuse.ai.server.manager.model.request.Gpt4oImageGenerateRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaEditRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaGenerateRequest;
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
    public ImageGenerateResponse gpt4oImageGenerate(Gpt4oImageGenerateRequest request) {
        return imageFeignClient.gpt4oImageGenerate(request);
    }

    /**
     * 生成/编辑flux-kontext图像
     */
    @Override
    public ImageGenerateResponse fluxKontextGenerate(FluxKontextImageRequest request) {
        return imageFeignClient.fluxKontextGenerate(request);
    }

    /**
     * 生成图像
     */
    @Override
    public ImageGenerateResponse nanoBananaGenerate(NanoBananaGenerateRequest request) {
        return imageFeignClient.nanoBananaGenerate(request);
    }

    /**
     * 编辑图像
     */
    @Override
    public ImageGenerateResponse nanoBananaEdit(NanoBananaEditRequest request) {
        return imageFeignClient.nanoBananaEdit(request);
    }

}
