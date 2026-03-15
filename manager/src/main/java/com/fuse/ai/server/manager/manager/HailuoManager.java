package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.HailuoImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.HailuoTextToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface HailuoManager {

    /**
     * Hailuo 文生视频
     */
    VideoGenerateResponse hailuoTextToVideo(HailuoTextToVideoRequest request, String apiKey);

    /**
     * Hailuo 图生视频
     */
    VideoGenerateResponse hailuoImageToVideo(HailuoImageToVideoRequest request, String apiKey);


}