package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.FluxKontextImageRequest;
import com.fuse.ai.server.manager.model.request.Gpt4oImageGenerateRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaEditRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;


public interface ImageManager {

    /**
     * 生成GPT-4o图像
     */
    ImageGenerateResponse gpt4oImageGenerate(Gpt4oImageGenerateRequest request);

    /**
     * 生成/编辑flux-kontext图像
     */
    ImageGenerateResponse fluxKontextGenerate(FluxKontextImageRequest request);

    /**
     * 生成图像
     */
    ImageGenerateResponse nanoBananaGenerate(NanoBananaGenerateRequest request);

    /**
     * 编辑图像
     */
    ImageGenerateResponse nanoBananaEdit(NanoBananaEditRequest request);

}
