package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Kling 3.0 运动控制请求
 * 模型示例: kling-3.0/motion-control
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Kling30MotionControlRequest extends KlingBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private Kling30MotionControlInput input;

    @Data
    public static class Kling30MotionControlInput {

        /**
         * 文本提示词，用于引导生成动画内容
         * 可选字段，可为空或 0-2500 字符
         */
        private String prompt;

        /**
         * 参考图片URL列表
         * 必填字段，包含一个图片URL
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * 参考视频URL列表
         * 必填字段，包含一个视频URL
         */
        @JsonProperty("video_urls")
        private List<String> videoUrls;

        /**
         * 视频质量模式
         * 可选字段
         * std: 标准模式 (720p)
         * pro: 专业模式 (1080p)
         */
        private String mode;

        /**
         * 角色朝向参考来源
         * 可选字段
         * video: 参考视频（推荐）
         * image: 参考图片
         * 默认值: video
         */
        @JsonProperty("character_orientation")
        private String characterOrientation;

        /**
         * 背景来源
         * 可选字段
         * input_video: 使用视频背景
         * input_image: 使用图片背景
         * 默认值: input_video
         */
        @JsonProperty("background_source")
        private String backgroundSource;
    }
}