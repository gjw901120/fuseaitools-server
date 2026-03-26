package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.video.WanTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.WanImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.WanVideoToVideoRequest;
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
}