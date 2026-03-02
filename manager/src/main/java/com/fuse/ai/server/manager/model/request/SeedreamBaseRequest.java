package com.fuse.ai.server.manager.model.request;

import lombok.Data;

/**
 * Seedream 请求基类，包含公共字段
 */
@Data
public class SeedreamBaseRequest {
    /**
     * 模型名称，例如：seedream/5-lite-text-to-image
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}