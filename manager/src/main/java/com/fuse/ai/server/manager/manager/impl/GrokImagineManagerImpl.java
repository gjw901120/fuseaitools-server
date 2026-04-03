package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.ImageFeignClient;
import com.fuse.ai.server.manager.feign.client.VideoFeignClient;
import com.fuse.ai.server.manager.manager.GrokImagineManager;
import com.fuse.ai.server.manager.model.request.image.GrokImagineImageToImageRequest;
import com.fuse.ai.server.manager.model.request.image.GrokImagineTextToImageRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineExtendRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.GrokImagineUpscaleRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrokImagineManagerImpl implements GrokImagineManager {

    @Autowired
    private ImageFeignClient imageFeignClient;

    @Autowired
    private VideoFeignClient videoFeignClient;

    @Override
    public ImageGenerateResponse textToImage(GrokImagineTextToImageRequest request, String apiKey) {
        return imageFeignClient.grokTextToImage(request, apiKey);
    }

    @Override
    public ImageGenerateResponse imageToImage(GrokImagineImageToImageRequest request, String apiKey) {
        return imageFeignClient.grokImageToImage(request, apiKey);
    }

    @Override
    public VideoGenerateResponse textToVideo(GrokImagineTextToVideoRequest request, String apiKey) {
        return videoFeignClient.grokTextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse imageToVideo(GrokImagineImageToVideoRequest request, String apiKey) {
        return videoFeignClient.grokImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse upscale(GrokImagineUpscaleRequest request, String apiKey) {
        return videoFeignClient.grokUpscale(request, apiKey);
    }

    @Override
    public VideoGenerateResponse extend(GrokImagineExtendRequest request, String apiKey) {
        return videoFeignClient.grokExtend(request, apiKey);
    }
}
