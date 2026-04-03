package com.fuse.ai.server.manager.model.request.video;

import lombok.Data;

/**
 * Grok Imagine 请求基类，包含公共字段
 */
@Data
public class GrokImagineBaseRequest {

    /**
     * 模型名称，例如：grok-imagine/text-to-video
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}