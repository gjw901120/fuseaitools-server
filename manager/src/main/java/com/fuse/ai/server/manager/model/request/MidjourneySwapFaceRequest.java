package com.fuse.ai.server.manager.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Swap Face 任务请求参数
 */
@Data
public class MidjourneySwapFaceRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 人脸源图片的 base64 编码
     */
    @NotBlank(message = "源图片不能为空")
    private String sourceBase64;

    /**
     * 目标图片的 base64 编码
     */
    @NotBlank(message = "目标图片不能为空")
    private String targetBase64;

    private String notifyHook;

    private String state;

    /**
     * 构建 Swap Face 请求
     */
    public static MidjourneySwapFaceRequest build(String sourceBase64, String targetBase64) {
        MidjourneySwapFaceRequest request = new MidjourneySwapFaceRequest();
        request.setSourceBase64(sourceBase64);
        request.setTargetBase64(targetBase64);
        return request;
    }
}