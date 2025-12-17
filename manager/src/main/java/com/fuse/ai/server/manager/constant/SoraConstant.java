package com.fuse.ai.server.manager.constant;

/**
 * Sora视频生成常量
 */
public final class SoraConstant {

    private SoraConstant() {}

    // 模型名称
    public static final String SORA_2_TEXT_TO_VIDEO = "sora-2-text-to-video";
    public static final String SORA_2_IMAGE_TO_VIDEO = "sora-2-image-to-video";
    public static final String SORA_2_PRO_TEXT_TO_VIDEO = "sora-2-pro-text-to-video";
    public static final String SORA_2_PRO_IMAGE_TO_VIDEO = "sora-2-pro-image-to-video";
    public static final String SORA_WATERMARK_REMOVER = "sora-watermark-remover";
    public static final String SORA_2_PRO_STORYBOARD = "sora-2-pro-storyboard";

    // 宽高比
    public static final String ASPECT_RATIO_PORTRAIT = "portrait";
    public static final String ASPECT_RATIO_LANDSCAPE = "landscape";

    // 帧数选项
    public static final String FRAMES_10 = "10";
    public static final String FRAMES_15 = "15";
    public static final String FRAMES_25 = "25";

    // 质量选项
    public static final String SIZE_STANDARD = "standard";
    public static final String SIZE_HIGH = "high";

    // 提示词最大长度
    public static final int PROMPT_MAX_LENGTH = 5000;
    public static final int VIDEO_URL_MAX_LENGTH = 500;
}