package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;

/**
 * 通义万相2 (Wan2.7) 请求基类，包含公共字段
 */
@Data
public class WanBaseRequest {
    /**
     * 模型名称
     */
    private String model = "";

    /**
     * 回调URL，任务完成时通知
     * 接收生成任务完成通知的回调 URL，可选配置，建议在生产环境中使用
     * 任务生成完成后，系统会向该URL POST任务状态与结果
     * 回调内容包含生成的资源URL与任务相关信息
     * 您的回调端点需要支持接收带JSON负载的POST请求
     * 也可以选择调用任务详情端点，主动轮询任务状态
     * 为确保回调安全性，请参阅Webhook校验指南了解签名验证实现方法
     */
    private String callBackUrl;
}