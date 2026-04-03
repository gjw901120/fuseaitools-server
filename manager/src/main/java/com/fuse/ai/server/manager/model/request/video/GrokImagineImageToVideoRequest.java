package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Grok Imagine 图像生成视频请求
 * 模型: grok-imagine/image-to-video
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GrokImagineImageToVideoRequest extends GrokImagineBaseRequest {

    /**
     * 视频生成任务的输入参数
     */
    private ImageToVideoInput input;

    @Data
    public static class ImageToVideoInput {

        /**
         * 外部图像 URL 列表作为视频生成的参考
         * 最多支持 7 个图像
         * 支持格式：JPEG、PNG、WEBP
         * 单张图像最大文件大小：10MB
         * 在提示词中引用时使用 @image(n) 语法
         * 使用外部图像时 Spicy 模式不可用
         */
        @JsonProperty("image_urls")
        private List<String> imageUrls;

        /**
         * 之前生成的 Grok 图像的任务 ID
         * 与 index 配合使用选择特定图像
         * 不要与 image_urls 同时使用
         * 最大长度：100 字符
         */
        @JsonProperty("task_id")
        private String taskId;

        /**
         * 使用 task_id 时，指定使用哪个图像
         * 基于 0 的索引 (0-5)
         * 默认值：0
         */
        private Integer index;

        /**
         * 描述期望视频运动的文本提示
         * 最大长度：5000 字符
         * 支持英文提示
         */
        private String prompt;

        /**
         * 指定影响运动风格和强度的生成模式
         * fun: 更有创意和趣味的解读
         * normal: 平衡方法，具有良好的运动质量
         * spicy: 更有活力和强烈的运动效果（外部图像不可用）
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

        /**
         * 图像比例选择，仅适用于多图生成模式
         * 单图模式下视频宽高参考图片宽高
         * 可选值：2:3, 3:2, 1:1, 16:9, 9:16
         * 默认值：16:9
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;
    }
}