package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;

public interface QwenManager {

    /**
     * Qwen 文生图
     */
    ImageGenerateResponse textToImage(QwenTextToImageRequest request, String apiKey);

    /**
     * Qwen 图生图
     */
    ImageGenerateResponse imageToImage(QwenImageToImageRequest request, String apiKey);

    /**
     * Qwen 图像编辑
     */
    ImageGenerateResponse imageEdit(QwenImageEditRequest request, String apiKey);

    /**
     * Qwen Z-Image
     */
    ImageGenerateResponse zImage(QwenZImageRequest request, String apiKey);

    ImageGenerateResponse v2TextToImage(Qwen2TextToImageRequest request, String apiKey);

    ImageGenerateResponse v2ImageEdit(Qwen2ImageEditRequest request, String apiKey);
}