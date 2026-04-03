package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Grok Imagine 视频放大请求
 * 模型: grok-imagine/upscale
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineUpscaleRequest extends GrokImagineBaseRequest {

    /**
     * 视频放大任务的输入参数
     */
    private UpscaleInput input;

    @Data
    public static class UpscaleInput {

        /**
         * 之前成功的图像生成任务的任务 ID
         * 必须来自 Kie AI 图像生成模型（例如 grok-imagine/text-to-image）
         * 原始图像生成必须成功完成
         * 最大长度：100 字符
         */
        @JsonProperty("task_id")
        private String taskId;
    }
}