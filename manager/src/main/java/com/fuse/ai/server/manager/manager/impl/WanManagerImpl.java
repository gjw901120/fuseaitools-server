package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.WanFeignClient;
import com.fuse.ai.server.manager.manager.WanManager;
import com.fuse.ai.server.manager.model.request.WanTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanVideoToVideoRequest;
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
}