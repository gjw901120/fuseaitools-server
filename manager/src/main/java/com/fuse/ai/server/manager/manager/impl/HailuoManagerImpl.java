package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.feign.client.HailuoFeignClient;
import com.fuse.ai.server.manager.manager.HailuoManager;
import com.fuse.ai.server.manager.model.request.HailuoImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.HailuoTextToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HailuoManagerImpl implements HailuoManager {

    @Autowired
    private HailuoFeignClient hailuoFeignClient;

    @Override
    public VideoGenerateResponse hailuoTextToVideo(HailuoTextToVideoRequest request, String apiKey) {
        return hailuoFeignClient.hailuoTextToVideo(request, apiKey);
    }

    @Override
    public VideoGenerateResponse hailuoImageToVideo(HailuoImageToVideoRequest request, String apiKey) {
        return hailuoFeignClient.hailuoImageToVideo(request, apiKey);
    }

}