package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.callback.image.*;

/**
 * 图像回调服务
 */
public interface ImageCallbackService {

    /**
     * 处理GPT-4O图像生成回调
     */
    void processGpt4oCallback(ImageGpt4oCallbackRequest request);

    /**
     * 处理Flux Kontext图像生成回调
     */
    void processFluxKontextCallback(ImageFluxKontextCallbackRequest request);

    /**
     * 处理Nano Banana图像生成回调
     */
    void processNanoBananaCallback(ImageNanoBananaCallbackRequest request);
}