package com.fuse.ai.server.manager.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class SoraStoryboardSceneRequest {

    @NotBlank(message = "场景描述不能为空")
    @Size(max = 5000, message = "场景描述长度不能超过5000个字符")
    private String scene;

    @NotNull(message = "持续时间不能为空")
    @Positive(message = "持续时间必须大于0")
    private Double duration;
}