package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.video.*;
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

    /**
     * Pro 15 图生视频
     */
    VideoGenerateResponse pro15ToVideo(Seedance15ProRequest request, String apiKey);

    VideoGenerateResponse v2Fast(Seedance2FastRequest request, String apiKey);

    VideoGenerateResponse v2(Seedance2Request request, String apiKey);

}