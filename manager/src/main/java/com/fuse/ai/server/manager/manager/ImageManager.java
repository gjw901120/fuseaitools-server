package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
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

}
