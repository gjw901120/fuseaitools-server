package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Grok Imagine 视频扩展请求
 * 模型: grok-imagine/extend
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineExtendRequest extends GrokImagineBaseRequest {

    /**
     * 视频扩展任务的输入参数
     */
    private ExtendInput input;

    @Data
    public static class ExtendInput {

        /**
         * 之前成功的视频生成任务的任务 ID
         * 必须来自 Kie AI 视频生成模型（例如 grok-imagine/text-to-video）
         * 原始视频生成必须成功完成
         * 最大长度：100 字符
         */
        @JsonProperty("task_id")
        private String taskId;

        /**
         * 描述所需视频运动的文本提示
         * 详细描述视频如何扩展和延续
         * 可以指定镜头运动、场景变化、物体动作等
         * 支持中英文输入
         */
        private String prompt;

        /**
         * 视频扩展的起点位置
         */
        @JsonProperty("extend_at")
        private Double extendAt;

        /**
         * 视频扩展的持续时间（秒）
         * 6: 扩展 6 秒视频内容
         * 10: 扩展 10 秒视频内容
         * 默认值：6
         */
        @JsonProperty("extend_times")
        private String extendTimes;
    }
}