package com.fuse.ai.server.manager.constant;

/**
 * Nano Banana图像生成相关常量
 */
public final class NanoBananaConstant {

    private NanoBananaConstant() {}

    // 模型类型
    public static final String MODEL_GENERATE = "google/nano-banana";
    public static final String MODEL_EDIT = "google/nano-banana-edit";

    public static final String MODEL_PRO_GENERATE = "nano-banana-pro";

    // 长宽比
    public static final String ASPECT_RATIO_1_1 = "1:1";
    public static final String ASPECT_RATIO_9_16 = "9:16";
    public static final String ASPECT_RATIO_16_9 = "16:9";
    public static final String ASPECT_RATIO_3_4 = "3:4";
    public static final String ASPECT_RATIO_4_3 = "4:3";
    public static final String ASPECT_RATIO_3_2 = "3:2";
    public static final String ASPECT_RATIO_2_3 = "2:3";
    public static final String ASPECT_RATIO_5_4 = "5:4";
    public static final String ASPECT_RATIO_4_5 = "4:5";
    public static final String ASPECT_RATIO_21_9 = "21:9";
    public static final String ASPECT_RATIO_AUTO = "auto";

    // 输出格式
    public static final String OUTPUT_FORMAT_JPEG = "jpeg";
    public static final String OUTPUT_FORMAT_PNG = "png";

    // 图像数量限制
    public static final int MAX_IMAGES_EDIT = 10;
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    // 提示词长度限制
    public static final int PROMPT_MAX_LENGTH = 5000;

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
}