package com.fuse.ai.server.manager.manager.impl;


import com.fuse.ai.server.manager.feign.client.VideoFeignClient;
import com.fuse.ai.server.manager.manager.VideoManager;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VideoManagerImpl implements VideoManager {

    @Autowired
    private VideoFeignClient videoFeignClient;

    @Override
    public VideoGenerateResponse veoGenerate(VeoGenerateRequest request, String apiKey) {

        return videoFeignClient.veoGenerate(request, apiKey);
    }

    @Override
    public VideoGenerateResponse runwayGenerate(RunwayGenerateRequest request, String apiKey) {
        return videoFeignClient.runwayGenerate(request, apiKey);
    }

    @Override
    public VideoGenerateResponse runwayExtend(RunwayExtendRequest request, String apiKey) {
        return videoFeignClient.runwayExtend(request, apiKey);
    }

    @Override
    public VideoGenerateResponse runwayAlephGenerate(RunwayAlephGenerateRequest request, String apiKey) {
        return videoFeignClient.alephGenerate(request, apiKey);
    }

    @Override
    public VideoGenerateResponse lumaModify(LumaGenerateRequest request, String apiKey) {
        return videoFeignClient.lumaModify(request, apiKey);
    }

    @Override
    public VideoGenerateResponse veoExtend(VeoExtendRequest request, String apiKey) {
        return videoFeignClient.veoExtend(request, apiKey);
    }
}