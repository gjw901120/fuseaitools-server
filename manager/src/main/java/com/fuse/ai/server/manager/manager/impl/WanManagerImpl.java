package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.WanFeignClient;
import com.fuse.ai.server.manager.manager.WanManager;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WanManagerImpl implements WanManager {

    @Autowired
    private WanFeignClient wanFeignClient;

    @Override
    public VideoGenerateResponse wanTextToVideo(WanTextToVideoRequest request, String apiKey) {
        return wanFeignClient.wanTextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wanImageToVideo(WanImageToVideoRequest request, String apiKey) {
        return wanFeignClient.wanImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wanVideoToVideo(WanVideoToVideoRequest request, String apiKey) {
        return wanFeignClient.wanVideoToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wan27TextToVideo(Wan27TextToVideoRequest request, String apiKey) {
        return wanFeignClient.wan27TextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wan27ImageToVideo(Wan27ImageToVideoRequest request, String apiKey) {
        return wanFeignClient.wan27ImageToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wan27VideoEdit(Wan27VideoEditRequest request, String apiKey) {
        return wanFeignClient.wan27VideoEdit(request, apiKey);
    }

    @Override
    public VideoGenerateResponse wan27R2V(Wan27R2VRequest request, String apiKey) {
        return wanFeignClient.wan27R2V(request, apiKey);
    }
}