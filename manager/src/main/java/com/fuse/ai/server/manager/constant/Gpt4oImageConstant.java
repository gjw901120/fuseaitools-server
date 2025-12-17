package com.fuse.ai.server.manager.constant;

/**
 * GPT-4o图像生成相关常量
 */
public final class Gpt4oImageConstant {

    private Gpt4oImageConstant() {}

    // 图片尺寸比例
    public static final String SIZE_1_1 = "1:1";
    public static final String SIZE_3_2 = "3:2";
    public static final String SIZE_2_3 = "2:3";

    // 变体数量
    public static final int VARIANTS_1 = 1;
    public static final int VARIANTS_2 = 2;
    public static final int VARIANTS_4 = 4;

    // 托底模型
    public static final String FALLBACK_MODEL_GPT_IMAGE_1 = "GPT_IMAGE_1";
    public static final String FALLBACK_MODEL_FLUX_MAX = "FLUX_MAX";

    // 文件限制
    public static final int MAX_FILES_COUNT = 5;
    public static final long MAX_MASK_FILE_SIZE = 25 * 1024 * 1024; // 25MB

    // 支持的文件格式
    public static final String[] SUPPORTED_IMAGE_FORMATS = {
            ".jfif", ".pjpeg", ".jpeg", ".pjp", ".jpg", ".png", ".webp"
    };

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INSUFFICIENT_CREDITS = 402;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_RATE_LIMIT = 429;
    public static final int CODE_SERVICE_UNAVAILABLE = 455;
    public static final int CODE_SERVER_ERROR = 500;
    public static final int CODE_CONNECTION_REFUSED = 550;
}