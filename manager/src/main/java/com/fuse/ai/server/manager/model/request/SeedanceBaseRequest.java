package com.fuse.ai.server.manager.model.request;

import lombok.Data;

/**
 * Seedance 请求基类，包含公共字段
 */
@Data
public class SeedanceBaseRequest {
    /**
     * 模型名称，例如：bytedance/v1-lite-text-to-video
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}