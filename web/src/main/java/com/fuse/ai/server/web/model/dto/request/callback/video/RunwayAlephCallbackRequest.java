package com.fuse.ai.server.web.model.dto.request.callback.video;

import lombok.Data;

/**
 * RunwayAleph回调请求
 */
@Data
public class RunwayAlephCallbackRequest {
    private Integer code;
    private String msg;
    private RunwayAlephCallbackData data;
    private String taskId;
}

