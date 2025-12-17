package com.fuse.ai.server.manager.constant;

/**
 * 视频转换相关常量
 */
public final class RunwayVideoAlephConstant {

    private RunwayVideoAlephConstant() {}

    // 最大文件大小 (10MB)
    public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;

    // 宽高比
    public static final String ASPECT_RATIO_16_9 = "16:9";
    public static final String ASPECT_RATIO_9_16 = "9:16";
    public static final String ASPECT_RATIO_4_3 = "4:3";
    public static final String ASPECT_RATIO_3_4 = "3:4";
    public static final String ASPECT_RATIO_1_1 = "1:1";
    public static final String ASPECT_RATIO_21_9 = "21:9";

    // 水印最大长度
    public static final int WATERMARK_MAX_LENGTH = 20;

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_IMAGE_ACCESS_ERROR = 451;
    public static final int CODE_SERVER_ERROR = 500;
}