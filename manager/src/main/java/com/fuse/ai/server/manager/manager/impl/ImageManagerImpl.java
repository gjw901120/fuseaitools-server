package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.ImageFeignClient;
import com.fuse.ai.server.manager.manager.ImageManager;
import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    @Override
    public ImageGenerateResponse nanoBanana2Generate(NanoBanana2Request request, String apiKey) {
        return imageFeignClient.nanoBanana2Generate(request, apiKey);
    }

    @Override
    public ImageGenerateResponse flux2ProImageToImage(Flux2ProImageToImageRequest request, String apiKey) {
        return imageFeignClient.flux2ProImageToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse flux2ProTextToImage(Flux2ProTextToImageRequest request, String apiKey) {
        return imageFeignClient.flux2ProTextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse flux2ImageToImage(Flux2ImageToImageRequest request, String apiKey) {
        return imageFeignClient.flux2ImageToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse flux2TextToImage(Flux2TextToImageRequest request, String apiKey) {
        return imageFeignClient.flux2TextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imagen4Ultra(Imagen4UltraRequest request, String apiKey) {
        return imageFeignClient.imagen4Ultra(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imagen4Fast(Imagen4FastRequest request, String apiKey) {
        return imageFeignClient.imagen4Fast(request, apiKey);
    }
    @Override
    public ImageGenerateResponse imagen4Generate(Imagen4GenerateRequest request, String apiKey) {
        return imageFeignClient.imagen4Generate(request, apiKey);
    }
}
