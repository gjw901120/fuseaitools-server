package com.fuse.ai.server.manager.constant;

/**
 * Luma视频修改相关常量
 */
public final class LumaVideoConstant {

    private LumaVideoConstant() {}

    // 文件大小限制 (500MB)
    public static final long MAX_FILE_SIZE_BYTES = 500 * 1024 * 1024;

    // 视频时长限制 (10秒)
    public static final int MAX_DURATION_SECONDS = 10;

    // 支持的视频格式
    public static final String[] SUPPORTED_FORMATS = {"mp4", "mov", "avi"};

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

    // 提示词语言要求
    public static final String PROMPT_LANGUAGE = "en";
}