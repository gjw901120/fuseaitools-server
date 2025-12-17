package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.LumaVideoConstant;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Luma视频修改请求参数
 */
@Data
public class LumaGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需视频修改的文本提示（仅支持英语）
     */
    @NotBlank(message = "提示词不能为空")
    @Size(min = 10, max = 2000, message = "提示词长度必须在10-2000个字符之间")
    private String prompt;

    /**
     * 用于修改的输入视频的URL
     */
    @NotBlank(message = "视频URL不能为空")
    @URL(message = "视频URL格式不正确")
    private String videoUrl;

    /**
     * 接收视频生成任务完成更新的URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 要添加到生成视频的水印标识符
     */
    @Size(max = 50, message = "水印标识符长度不能超过50个字符")
    private String watermark;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 验证视频URL格式
        validateVideoUrlFormat();

        // 验证提示词语言（基本检查）
        validatePromptLanguage();
    }

    /**
     * 验证视频URL格式
     */
    private void validateVideoUrlFormat() {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // 检查文件扩展名
            String lowerUrl = videoUrl.toLowerCase();
            boolean supportedFormat = false;
            for (String format : LumaVideoConstant.SUPPORTED_FORMATS) {
                if (lowerUrl.endsWith("." + format)) {
                    supportedFormat = true;
                    break;
                }
            }

            if (!supportedFormat) {
                throw new IllegalArgumentException(
                        "不支持的视频格式，支持的格式: " + String.join(", ", LumaVideoConstant.SUPPORTED_FORMATS)
                );
            }
        }
    }

    /**
     * 基本验证提示词语言（主要检查是否包含中文字符）
     */
    private void validatePromptLanguage() {
        if (prompt != null && !prompt.isEmpty()) {
            // 简单检查是否包含中文字符
            if (prompt.matches(".*[\\u4e00-\\u9fa5]+.*")) {
                throw new IllegalArgumentException("提示词必须为英文，不支持中文");
            }
        }
    }

    /**
     * 构建默认请求
     */
    public static LumaGenerateRequest of(String prompt, String videoUrl) {
        LumaGenerateRequest request = new LumaGenerateRequest();
        request.setPrompt(prompt);
        request.setVideoUrl(videoUrl);
        return request;
    }

    /**
     * 构建带回调的请求
     */
    public static LumaGenerateRequest of(String prompt, String videoUrl, String callBackUrl) {
        LumaGenerateRequest request = of(prompt, videoUrl);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建完整请求
     */
    public static LumaGenerateRequest of(String prompt, String videoUrl, String callBackUrl, String watermark) {
        LumaGenerateRequest request = of(prompt, videoUrl, callBackUrl);
        request.setWatermark(watermark);
        return request;
    }
}