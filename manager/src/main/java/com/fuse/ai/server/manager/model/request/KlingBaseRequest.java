package com.fuse.ai.server.manager.model.request;

import lombok.Data;

/**
 * Kling 请求基类，包含公共字段
 */
@Data
public class KlingBaseRequest {
    /**
     * 模型名称，例如：kling/v2-5-turbo-text-to-video-pro
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}