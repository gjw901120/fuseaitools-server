package com.fuse.ai.server.manager.model.request.video;

import lombok.Data;

/**
 * Hailuo 请求基类，包含公共字段
 */
@Data
public class HailuoBaseRequest {
    /**
     * 模型名称，例如：hailuo/2-3-image-to-video-standard
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}