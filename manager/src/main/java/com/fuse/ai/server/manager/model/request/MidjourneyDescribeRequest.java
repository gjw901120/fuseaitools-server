package com.fuse.ai.server.manager.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * Describe 任务请求参数
 */
@Data
public class MidjourneyDescribeRequest {

    private static final long serialVersionUID = 1L;

    private String botType = "MID_JOURNEY";

    /**
     * 需要描述的图片的 base64 编码
     */
    @NotBlank(message = "图片base64编码不能为空")
    private String base64;

    private String notifyHook;

    private String state;

}