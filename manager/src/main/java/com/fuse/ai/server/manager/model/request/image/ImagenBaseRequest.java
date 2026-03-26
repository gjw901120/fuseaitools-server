package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Imagen 请求基类，包含公共字段
 */
@Data
public class ImagenBaseRequest implements Serializable {

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
     * 接收生成任务完成通知的回调 URL
     * 任务生成完成后，系统会向该 URL POST 任务状态与结果
     */
    private String callBackUrl;
}