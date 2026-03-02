package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface SeedanceManager {

    /**
     * Lite 文生视频
     */
    VideoGenerateResponse liteTextToVideo(SeedanceLiteTextToVideoRequest request, String apiKey);

    /**
     * Lite 图生视频
     */
    VideoGenerateResponse liteImageToVideo(SeedanceLiteImageToVideoRequest request, String apiKey);

    /**
     * Pro 文生视频
     */
    VideoGenerateResponse proTextToVideo(SeedanceProTextToVideoRequest request, String apiKey);

    /**
     * Pro 图生视频
     */
    VideoGenerateResponse proImageToVideo(SeedanceProImageToVideoRequest request, String apiKey);

    /**
     * Pro Fast 图生视频
     */
    VideoGenerateResponse proFastImageToVideo(SeedanceProFastImageToVideoRequest request, String apiKey);
}