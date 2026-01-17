package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.SoraGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;


public interface SoraManager {

    VideoGenerateResponse generateVideo(SoraGenerateRequest request, String apiKey);

    VideoGenerateResponse soraWatermarkRemover(SoraGenerateRequest request, String apiKey);

    VideoGenerateResponse soraStoryboard(SoraGenerateRequest request, String apiKey);

}
