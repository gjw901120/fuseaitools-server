package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SoraAspectRatioEnum;
import com.fuse.ai.server.manager.enums.SoraFramesEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SoraStoryboardRequest {

    @NotNull(message = "视频总长度不能为空")
    private SoraFramesEnum nFrames;

    private List<@URL(message = "图片URL格式不正确") String> imageUrls;

    private SoraAspectRatioEnum aspectRatio;

    @NotEmpty(message = "场景列表不能为空")
    @NotNull(message = "场景列表不能为null")
    private List<SoraStoryboardSceneRequest> shots;
}