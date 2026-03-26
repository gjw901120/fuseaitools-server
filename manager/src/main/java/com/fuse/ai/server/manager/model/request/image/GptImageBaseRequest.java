package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;

/**
 * GPT Image 请求基类，包含公共字段
 */
@Data
public class GptImageBaseRequest {
    /**
     * 模型名称，例如：gpt-image/1.5-text-to-image
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}