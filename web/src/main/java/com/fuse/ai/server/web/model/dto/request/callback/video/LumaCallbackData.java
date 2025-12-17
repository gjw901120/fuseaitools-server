package com.fuse.ai.server.web.model.dto.request.callback.video;

import lombok.Data;

import java.util.List;

/**
 * Luma回调数据
 */
@Data
public class LumaCallbackData {
    private String taskId;

    private String promptJson;

    private List<String> resultUrls;
}