package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Runway回调数据
 */
@Data
public class RunwayCallbackData {
    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("video_id")
    private String videoId;

    @JsonProperty("video_url")
    private String videoUrl;

    @JsonProperty("image_url")
    private String imageUrl;
}