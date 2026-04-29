package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.GptImageFeignClient;
import com.fuse.ai.server.manager.manager.GptImageManager;
import com.fuse.ai.server.manager.model.request.image.GptImageImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageTextToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2ImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GptImageV2TextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptImageManagerImpl implements GptImageManager {

    @Autowired
    private GptImageFeignClient gptImageFeignClient;

    @Override
    public ImageGenerateResponse gptImageTextToImage(GptImageTextToImageRequest request, String apiKey) {
        return gptImageFeignClient.gptImageTextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse gptImageImageToImage(GptImageImageToImageRequest request, String apiKey) {
        return gptImageFeignClient.gptImageImageToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse gptImageV2TextToImage(GptImageV2TextToImageRequest request, String apiKey) {
        return gptImageFeignClient.gptImageV2TextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse gptImageV2ImageToImage(GptImageV2ImageToImageRequest request, String apiKey) {
        return gptImageFeignClient.gptImageV2ImageToImage(request, apiKey);
    }
}