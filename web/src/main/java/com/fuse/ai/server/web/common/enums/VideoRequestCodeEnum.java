package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Veo视频请求状态码枚举
 */
@Getter
public enum VideoRequestCodeEnum {

    /**
     * 成功 - 视频生成任务成功
     */
    SUCCESS(200, "Success", "视频生成任务成功"),

    /**
     * 客户端错误 - 提示词违反内容政策或其他输入错误
     */
    CLIENT_ERROR(400, "Client Error", "提示词违反内容政策或其他输入错误"),

    /**
     * 托底失败 - 当未开启托底且遇到特定错误时返回
     */
    FALLBACK_ERROR(422, "Fallback Error", "Your request was rejected by Flow(原始错误信息). You may consider using our other fallback channels, which are likely to succeed. Please refer to the documentation."),

    /**
     * 内部错误 - 请稍后重试，内部错误或超时
     */
    INTERNAL_ERROR(500, "Internal Error", "内部错误，请稍后重试"),

    /**
     * 失败 - 视频生成任务失败
     */
    GENERATION_FAILED(501, "Generation Failed", "视频生成任务失败");

    private final Integer code;
    private final String message;
    private final String description;

    VideoRequestCodeEnum(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * 根据状态码获取枚举
     */
    public static VideoRequestCodeEnum getByCode(Integer code) {
        for (VideoRequestCodeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return INTERNAL_ERROR;
    }

    /**
     * 判断是否是成功状态码
     */
    public static boolean isSuccess(Integer code) {
        return code != null && code == 200;
    }

    /**
     * 判断是否是客户端错误
     */
    public static boolean isClientError(Integer code) {
        return code != null && code >= 400 && code < 500;
    }

    /**
     * 判断是否是服务端错误
     */
    public static boolean isServerError(Integer code) {
        return code != null && code >= 500;
    }

    /**
     * 获取托底错误消息（包含原始错误信息）
     */
    public static String getFallbackErrorMessage(String originalError) {
        return FALLBACK_ERROR.getDescription().replace("原始错误信息", originalError);
    }
}