package com.fuse.ai.server.manager.constant;

/**
 * Veo视频生成相关常量
 */
public final class VeoVideoConstant {

    private VeoVideoConstant() {}

    // 模型类型
    public static final String MODEL_VEO3 = "veo3";
    public static final String MODEL_VEO3_FAST = "veo3_fast";

    // 生成模式
    public static final String GENERATION_TYPE_TEXT_2_VIDEO = "TEXT_2_VIDEO";
    public static final String GENERATION_TYPE_FIRST_AND_LAST_FRAMES_2_VIDEO = "FIRST_AND_LAST_FRAMES_2_VIDEO";
    public static final String GENERATION_TYPE_REFERENCE_2_VIDEO = "REFERENCE_2_VIDEO";

    // 宽高比
    public static final String ASPECT_RATIO_16_9 = "16:9";
    public static final String ASPECT_RATIO_9_16 = "9:16";
    public static final String ASPECT_RATIO_AUTO = "Auto";

    // 图片数量限制
    public static final int MIN_IMAGE_URLS = 1;
    public static final int MAX_IMAGE_URLS = 3;
    public static final int MAX_IMAGE_URLS_FOR_REFERENCE = 3;
    public static final int MAX_IMAGE_URLS_FOR_FIRST_LAST = 2;

    // 随机种子范围
    public static final int SEED_MIN = 10000;
    public static final int SEED_MAX = 99999;

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INSUFFICIENT_CREDITS = 402;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_RATE_LIMIT = 429;
    public static final int CODE_SERVICE_UNAVAILABLE = 455;
    public static final int CODE_SERVER_ERROR = 500;
    public static final int CODE_GENERATION_FAILED = 501;
    public static final int CODE_FEATURE_DISABLED = 505;
}