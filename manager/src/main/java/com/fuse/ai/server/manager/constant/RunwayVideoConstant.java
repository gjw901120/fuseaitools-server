package com.fuse.ai.server.manager.constant;

public class RunwayVideoConstant {

    private RunwayVideoConstant() {}

    // 视频时长
    public static final int DURATION_5S = 5;
    public static final int DURATION_10S = 10;

    // 视频质量
    public static final String QUALITY_720P = "720p";
    public static final String QUALITY_1080P = "1080p";

    // 宽高比
    public static final String ASPECT_RATIO_16_9 = "16:9";
    public static final String ASPECT_RATIO_4_3 = "4:3";
    public static final String ASPECT_RATIO_1_1 = "1:1";
    public static final String ASPECT_RATIO_3_4 = "3:4";
    public static final String ASPECT_RATIO_9_16 = "9:16";

    // 响应码
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_VALIDATION_ERROR = 422;
    public static final int CODE_IMAGE_ACCESS_DENIED = 451;
    public static final int CODE_SERVICE_UNAVAILABLE = 455;
    public static final int CODE_SERVER_ERROR = 500;

    // 提示词最大长度
    public static final int PROMPT_MAX_LENGTH = 1800;
}

