package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.enums.SoraAspectRatioEnum;
import com.fuse.ai.server.manager.enums.SoraFramesEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SoraInputBaseRequest {

    @NotBlank(message = "提示词不能为空")
    @Size(max = 5000, message = "提示词长度不能超过5000个字符")
    private String prompt;

    @JsonProperty("aspect_ratio")
    private SoraAspectRatioEnum aspectRatio;

    @JsonProperty("n_frames")
    private SoraFramesEnum nFrames;

    @JsonProperty("remove_watermark")
    private Boolean removeWatermark;
}