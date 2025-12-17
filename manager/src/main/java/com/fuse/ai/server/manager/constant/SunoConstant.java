package com.fuse.ai.server.manager.constant;

/**
 * Suno 音乐生成相关常量
 */
public final class SunoConstant {

    private SunoConstant() {}

    // 模型版本
    public static final String MODEL_V3_5 = "V3_5";
    public static final String MODEL_V4 = "V4";
    public static final String MODEL_V4_5 = "V4_5";
    public static final String MODEL_V4_5PLUS = "V4_5PLUS";
    public static final String MODEL_V5 = "V5";

    // 人声性别
    public static final String VOCAL_GENDER_MALE = "m";
    public static final String VOCAL_GENDER_FEMALE = "f";

    // 字符长度限制
    public static final int PROMPT_MAX_LENGTH_V3_5_V4 = 3000;
    public static final int PROMPT_MAX_LENGTH_V4_5_PLUS_V5 = 5000;
    public static final int PROMPT_MAX_LENGTH_SIMPLE = 500;
    public static final int STYLE_MAX_LENGTH_V3_5_V4 = 200;
    public static final int STYLE_MAX_LENGTH_V4_5_PLUS_V5 = 1000;
    public static final int TITLE_MAX_LENGTH_V3_5_V4 = 80;
    public static final int TITLE_MAX_LENGTH_V4_5_PLUS_V5 = 100;

    // 数值范围限制
    public static final double MIN_WEIGHT = 0.0;
    public static final double MAX_WEIGHT = 1.0;

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INSUFFICIENT_CREDITS = 402;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_CONFLICT = 409;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_RATE_LIMIT = 429;
    public static final int CODE_UNAUTHORIZED_IMAGE = 451;
    public static final int CODE_SERVICE_UNAVAILABLE = 455;
    public static final int CODE_SERVER_ERROR = 500;
}