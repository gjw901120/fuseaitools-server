package com.fuse.ai.server.manager.constant;

/**
 * Midjourney 相关常量
 */
public final class MidjourneyConstant {

    private MidjourneyConstant() {}

    // Bot 类型
    public static final String BOT_TYPE_MID_JOURNEY = "MID_JOURNEY";
    public static final String BOT_TYPE_NIJI_JOURNEY = "NIJI_JOURNEY";

    // 任务动作类型
    public static final String ACTION_IMAGINE = "IMAGINE";
    public static final String ACTION_UPSCALE = "UPSCALE";
    public static final String ACTION_VARIATION = "VARIATION";
    public static final String ACTION_ZOOM = "ZOOM";
    public static final String ACTION_PAN = "PAN";
    public static final String ACTION_DESCRIBE = "DESCRIBE";
    public static final String ACTION_BLEND = "BLEND";
    public static final String ACTION_SHORTEN = "SHORTEN";
    public static final String ACTION_SWAP_FACE = "SWAP_FACE";

    // 图片尺寸
    public static final String DIMENSIONS_SQUARE = "SQUARE";
    public static final String DIMENSIONS_PORTRAIT = "PORTRAIT";
    public static final String DIMENSIONS_LANDSCAPE = "LANDSCAPE";

    // 账号模式
    public static final String MODE_RELAX = "RELAX";
    public static final String MODE_FAST = "FAST";
    public static final String MODE_TURBO = "TURBO";

    // 任务状态
    public static final String STATUS_NOT_START = "NOT_START";
    public static final String STATUS_SUBMITTED = "SUBMITTED";
    public static final String STATUS_MODAL = "MODAL";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_FAILURE = "FAILURE";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_CANCEL = "CANCEL";

    // 响应码
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_IN_QUEUE = 22;
    public static final int CODE_PARAM_ERROR = 21;
    public static final int CODE_SYSTEM_ERROR = 23;
    public static final int CODE_ACCOUNT_UNAVAILABLE = 24;
    public static final int CODE_INSUFFICIENT_BALANCE = 25;

    // 文件限制
    public static final long MAX_FILE_SIZE = 4 * 1024 * 1024; // 4MB
    public static final int MAX_BLEND_IMAGES = 5;
    public static final int MIN_BLEND_IMAGES = 2;

    // Base64 前缀
    public static final String BASE64_PREFIX_JPEG = "data:image/jpeg;base64,";
    public static final String BASE64_PREFIX_PNG = "data:image/png;base64,";
    public static final String BASE64_PREFIX_GIF = "data:image/gif;base64,";
    public static final String BASE64_PREFIX_WEBP = "data:image/webp;base64,";
}