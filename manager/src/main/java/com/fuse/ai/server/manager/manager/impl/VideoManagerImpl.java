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
    public VideoGenerateResponse veoGenerate(VeoGenerateRequest request) {

        return videoFeignClient.veoGenerate(request);
    }

    @Override
    public VideoGenerateResponse runwayGenerate(RunwayGenerateRequest request) {
        return videoFeignClient.runwayGenerate(request);
    }

    @Override
    public VideoGenerateResponse runwayExtend(RunwayExtendRequest request) {
        return videoFeignClient.runwayExtend(request);
    }

    @Override
    public VideoGenerateResponse runwayAlephGenerate(RunwayAlephGenerateRequest request) {
        return videoFeignClient.alephGenerate(request);
    }

    @Override
    public VideoGenerateResponse lumaModify(LumaGenerateRequest request) {
        return videoFeignClient.lumaModify(request);
    }

    @Override
    public VideoGenerateResponse veoExtend(VeoExtendRequest request) {
        return videoFeignClient.veoExtend(request);
    }
}