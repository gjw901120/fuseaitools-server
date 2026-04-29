package com.fuse.ai.server.manager.model.request.video;

import lombok.Data;

/**
 * HappyHorse 请求基类，包含公共字段
 */
@Data
public class HappyHorseBaseRequest {
    /**
     * 模型名称，例如：happyhorse/text-to-video
     */
    private String model;

    /**
     * 回调URL，任务完成时通知
     */
    private String callBackUrl;
}