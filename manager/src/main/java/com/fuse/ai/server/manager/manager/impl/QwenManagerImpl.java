package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.QwenFeignClient;
import com.fuse.ai.server.manager.manager.QwenManager;
import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QwenManagerImpl implements QwenManager {

    @Autowired
    private QwenFeignClient qwenFeignClient;

    @Override
    public ImageGenerateResponse textToImage(QwenTextToImageRequest request, String apiKey) {
        return qwenFeignClient.textToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imageToImage(QwenImageToImageRequest request, String apiKey) {
        return qwenFeignClient.imageToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imageEdit(QwenImageEditRequest request, String apiKey) {
        return qwenFeignClient.imageEdit(request, apiKey);
    }

    @Override
    public ImageGenerateResponse zImage(QwenZImageRequest request, String apiKey) {
        return qwenFeignClient.zImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse v2TextToImage(Qwen2TextToImageRequest request, String apiKey) {
        return qwenFeignClient.v2TextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse v2ImageEdit(Qwen2ImageEditRequest request, String apiKey) {
        return qwenFeignClient.v2ImageEdit(request, apiKey);
    }

}