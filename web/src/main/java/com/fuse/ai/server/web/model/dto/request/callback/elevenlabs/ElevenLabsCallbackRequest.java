package com.fuse.ai.server.web.model.dto.request.callback.elevenlabs;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ElevenLabs回调请求
 */
@Data
public class ElevenLabsCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private ElevenLabsCallbackData data;

    /**
     * 消息
     */
    private String msg;
}