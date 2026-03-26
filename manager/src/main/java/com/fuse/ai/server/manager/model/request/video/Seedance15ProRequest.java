package com.fuse.ai.server.manager.model.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Seedance 1.5 Pro 文本/图像生成视频请求实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Seedance15ProRequest extends SeedanceBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private Seedance15ProInput input;

    @Data
    public static class Seedance15ProInput {

        /**
         * 用于视频生成的文本提示词
         * 必填字段，长度限制：3-2500字符
         */
        private String prompt;

        /**
         * 用于图生视频的输入图片 URL
         * 可选字段，支持 0-2 张图片
         * 若不提供，模型将执行文生视频
         * 支持的格式：image/jpeg、image/png、image/webp
         * 单张图片最大大小：10.0MB
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * 视频画面比例配置
         * 必填字段
         * 可选值：1:1, 4:3, 3:4, 16:9, 9:16, 21:9
         * 默认值：1:1
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 视频分辨率
         * 可选字段
         * 可选值：480p, 720p, 1080p
         * 默认值：720p
         * 480p: 生成速度更快
         * 720p: 兼顾速度与画质
         * 1080p: 画质更高
         */
        private String resolution;

        /**
         * 视频时长（单位：秒）
         * 可选字段
         * 可选值：4, 8, 12
         * 默认值：8
         */
        private Integer duration;

        /**
         * 锁定摄像机，实现静态拍摄
         * 可选字段
         * true: 锁定摄像机，实现静态拍摄
         * false: 允许动态摄像机移动
         * 默认值：false
         */
        @JsonProperty("fixed_lens")
        private Boolean fixedLens;

        /**
         * 是否为视频生成音频
         * 可选字段
         * true: 生成带音频的视频（费用更高）
         * false: 生成无音频的视频
         * 默认值：false
         */
        @JsonProperty("generate_audio")
        private Boolean generateAudio;

        /**
         * 是否启用 NSFW 内容检测器
         * 可选字段
         * Playground 中默认启用，API 调用可根据需要配置
         * 默认值：false
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker;

    }
}