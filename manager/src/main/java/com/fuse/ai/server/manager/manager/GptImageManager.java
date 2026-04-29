package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.image.GptImageTextToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2ImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2TextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;

public interface GptImageManager {

    /**
     * GPT Image 1.5 文生图
     */
    ImageGenerateResponse gptImageTextToImage(GptImageTextToImageRequest request, String apiKey);

    /**
     * GPT Image 1.5 图生图
     */
    ImageGenerateResponse gptImageImageToImage(GptImageImageToImageRequest request, String apiKey);

    ImageGenerateResponse gptImageV2TextToImage(GptImageV2TextToImageRequest request, String apiKey);

    ImageGenerateResponse gptImageV2ImageToImage(GptImageV2ImageToImageRequest request, String apiKey);
}