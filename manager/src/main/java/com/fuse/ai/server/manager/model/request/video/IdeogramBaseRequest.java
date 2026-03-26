package com.fuse.ai.server.manager.model.request.video;

import lombok.Data;

/**
 * Ideogram 请求基类，包含公共字段
 */
@Data
public class IdeogramBaseRequest {
    /**
     * 模型名称，例如：ideogram/v3-text-to-image
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}