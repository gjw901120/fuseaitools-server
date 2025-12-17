package com.fuse.ai.server.manager.constant;

/**
 * Flux Kontext图像生成相关常量
 */
public final class FluxKontextConstant {

    private FluxKontextConstant() {}

    // 模型类型
    public static final String MODEL_FLUX_KONTEXT_PRO = "flux-kontext-pro";
    public static final String MODEL_FLUX_KONTEXT_MAX = "flux-kontext-max";

    // 长宽比
    public static final String ASPECT_RATIO_21_9 = "21:9";
    public static final String ASPECT_RATIO_16_9 = "16:9";
    public static final String ASPECT_RATIO_4_3 = "4:3";
    public static final String ASPECT_RATIO_1_1 = "1:1";
    public static final String ASPECT_RATIO_3_4 = "3:4";
    public static final String ASPECT_RATIO_9_16 = "9:16";

    // 输出格式
    public static final String OUTPUT_FORMAT_JPEG = "jpeg";
    public static final String OUTPUT_FORMAT_PNG = "png";

    // 安全容忍度范围
    public static final int SAFETY_TOLERANCE_MIN = 0;
    public static final int SAFETY_TOLERANCE_MAX_GENERATE = 6;
    public static final int SAFETY_TOLERANCE_MAX_EDIT = 2;
    public static final int SAFETY_TOLERANCE_DEFAULT = 2;

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