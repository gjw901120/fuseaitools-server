package com.fuse.ai.server.manager.model.request.image;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Nano Banana 请求基类，包含公共字段
 */
@Data
public class NanoBananaBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
    **/
    private String model;

    /**
     * 任务完成时通知此URL
     */
    private String callBackUrl;
}