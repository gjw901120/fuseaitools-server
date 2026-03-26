package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Flux 请求基类，包含公共字段
 */
@Data
public class FluxBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     * 必填字段
     */
    private String model;

    /**
     * 回调 URL
     * 可选字段
     * 接收生成任务完成更新的 URL
     * 当生成完成时，系统将向此 URL POST 任务状态和结果
     */
    private String callBackUrl;
}