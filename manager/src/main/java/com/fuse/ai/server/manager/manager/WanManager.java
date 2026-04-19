package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface WanManager {

    /**
     * Wan 文生视频
     */
    VideoGenerateResponse wanTextToVideo(WanTextToVideoRequest request, String apiKey);

    /**
     * Wan 图生视频
     */
    VideoGenerateResponse wanImageToVideo(WanImageToVideoRequest request, String apiKey);

    /**
     * Wan 视频生视频
     */
    VideoGenerateResponse wanVideoToVideo(WanVideoToVideoRequest request, String apiKey);

    VideoGenerateResponse wan27TextToVideo(Wan27TextToVideoRequest request, String apiKey);

    VideoGenerateResponse wan27ImageToVideo(Wan27ImageToVideoRequest request, String apiKey);

    VideoGenerateResponse wan27VideoEdit(Wan27VideoEditRequest request, String apiKey);

    VideoGenerateResponse wan27R2V(Wan27R2VRequest request, String apiKey);
}