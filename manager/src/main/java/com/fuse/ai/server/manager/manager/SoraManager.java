package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.SoraGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;


public interface SoraManager {

    VideoGenerateResponse generateVideo(SoraGenerateRequest request);

    VideoGenerateResponse soraWatermarkRemover(SoraGenerateRequest request);

    VideoGenerateResponse soraStoryboard(SoraGenerateRequest request);

}
