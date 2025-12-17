package com.fuse.ai.server.manager.constant;

/**
 * Veo视频扩展相关常量
 */
public final class VeoVideoExtendConstant {

    private VeoVideoExtendConstant() {}

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