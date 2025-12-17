package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.callback.video.*;

/**
 * 视频回调服务
 */
public interface VideoCallbackService {

    /**
     * 处理Veo回调
     */
    void VeoCallback(VeoCallbackRequest request);

    /**
     * 处理Runway回调
     */
    void RunwayCallback(RunwayCallbackRequest request);

    /**
     * 处理RunwayAleph回调
     */
    void RunwayAlephCallback(RunwayAlephCallbackRequest request);

    /**
     * 处理Luma回调
     */
    void LumaCallback(LumaCallbackRequest request);

    /**
     * 处理Sora回调
     */
    void SoraCallback(SoraCallbackRequest request);

}