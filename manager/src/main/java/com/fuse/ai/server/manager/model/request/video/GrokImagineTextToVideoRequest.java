package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Grok Imagine 文本生成视频请求
 * 模型: grok-imagine/text-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineTextToVideoRequest extends GrokImagineBaseRequest {

    /**
     * 视频生成任务的输入参数
     */
    private TextToVideoInput input;

    @Data
    public static class TextToVideoInput {

        /**
         * 描述期望视频运动的文本提示
         * 最大长度：5000 字符
         * 支持英文提示
         */
        private String prompt;

        /**
         * 指定生成视频的宽高比
         * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
         * 默认值：2:3
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 指定影响运动风格和强度的生成模式
         * fun: 更有创意和趣味的解读
         * normal: 平衡方法，具有良好的运动质量
         * spicy: 更有活力和强烈的运动效果
         * 默认值：normal
         */
        private String mode;

        /**
         * 生成的视频时长（秒）
         * 最小值：6，最大值：30，步长：1
         */
        private BigDecimal duration;

        /**
         * 生成视频的分辨率
         * 可选值：480p, 720p
         * 默认值：480p
         */
        private String resolution;
    }
}