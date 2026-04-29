package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ReferenceToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1TextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1VideoEditRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface HappyHorseManager {
    VideoGenerateResponse v1TextToVideo(HappyHorseV1TextToVideoRequest request, String apiKey);

    VideoGenerateResponse v1ImageToVideo(HappyHorseV1ImageToVideoRequest request, String apiKey);

    VideoGenerateResponse v1ReferenceToVideo(HappyHorseV1ReferenceToVideoRequest request, String apiKey);

    VideoGenerateResponse v1VideoEdit(HappyHorseV1VideoEditRequest request, String apiKey);
}
