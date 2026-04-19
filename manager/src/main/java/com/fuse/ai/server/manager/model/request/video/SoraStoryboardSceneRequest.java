package com.fuse.ai.server.manager.model.request.video;

import lombok.Data;

@Data
public class SoraStoryboardSceneRequest {

    private String scene;

    private Double duration;
}