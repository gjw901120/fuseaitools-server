package com.fuse.ai.server.web.model.dto.request.callback.video;

import lombok.Data;

/**
 * 基础回调请求结构（泛型）
 */
@Data
public class BaseCallbackRequest<T> {

    private Integer code;
    private String msg;
    private T data;
}