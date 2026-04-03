package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.callback.video.*;

/**
 * 视频回调服务
 */
public interface VideoCallbackService {

    /**
     * 处理Veo回调
     */
    void veoCallback(VeoCallbackRequest request);

    /**
     * 处理Runway回调
     */
    void runwayCallback(RunwayCallbackRequest request);

    /**
     * 处理RunwayAleph回调
     */
    void runwayAlephCallback(RunwayAlephCallbackRequest request);

    /**
     * 处理Luma回调
     */
    void lumaCallback(LumaCallbackRequest request);

    /**
     * 处理Sora回调
     */
    void soraCallback(SoraCallbackRequest request);

    void seedanceCallback(SeedanceCallbackRequest request);

    void wanCallback(WanCallbackRequest request);

    void klingCallback(KlingCallbackRequest request);
    void hailuoCallback(HailuoCallbackRequest request);

    void grokCallback(VideoCallbackRequest request);

}