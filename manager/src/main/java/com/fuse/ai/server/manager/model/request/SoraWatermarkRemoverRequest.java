package com.fuse.ai.server.manager.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SoraWatermarkRemoverRequest {

    @NotBlank(message = "视频URL不能为空")
    @Size(max = 500, message = "视频URL长度不能超过500个字符")
    @URL(message = "视频URL格式不正确")
    private String videoUrl;
}