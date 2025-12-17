package com.fuse.ai.server.web.model.dto.request.callback.video;

import lombok.Data;

/**
 * Veo回调数据
 */
@Data
public class  VeoCallbackData {
    private String taskId;
    private VeoVideoInfo info;
    private Boolean fallbackFlag;


    /**
     * Veo视频信息
     */
    @Data
    public static class VeoVideoInfo {
        private java.util.List<String> resultUrls;
        private java.util.List<String> originUrls;
        private String resolution;
    }
}
