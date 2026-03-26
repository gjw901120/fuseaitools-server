package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;


public interface ImageManager {

    /**
     * 生成GPT-4o图像
     */
    ImageGenerateResponse gpt4oImageGenerate(Gpt4oImageGenerateRequest request, String apiKey);

    /**
     * 生成/编辑flux-kontext图像
     */
    ImageGenerateResponse fluxKontextGenerate(FluxKontextImageRequest request, String apiKey);

    /**
     * 生成图像
     */
    ImageGenerateResponse nanoBananaGenerate(NanoBananaGenerateRequest request, String apiKey);

    /**
     * 生成图像Pro
     */
    ImageGenerateResponse nanoBananaProGenerate(NanoBananaProGenerateRequest request, String apiKey);

    /**
     * 编辑图像
     */
    ImageGenerateResponse nanoBananaEdit(NanoBananaEditRequest request, String apiKey);

    ImageGenerateResponse nanoBanana2Generate(NanoBanana2Request request, String apiKey);

    ImageGenerateResponse flux2ProImageToImage(Flux2ProImageToImageRequest request, String apiKey);

    ImageGenerateResponse flux2ProTextToImage(Flux2ProTextToImageRequest request, String apiKey);

    ImageGenerateResponse flux2ImageToImage(Flux2ImageToImageRequest request, String apiKey);

    ImageGenerateResponse flux2TextToImage(Flux2TextToImageRequest request, String apiKey);

    ImageGenerateResponse imagen4Ultra(Imagen4UltraRequest request, String apiKey);
    ImageGenerateResponse imagen4Generate(Imagen4GenerateRequest request, String apiKey);
    ImageGenerateResponse imagen4Fast(Imagen4FastRequest request, String apiKey);

}
