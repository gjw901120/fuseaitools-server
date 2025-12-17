package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.RunwayVideoAlephConstant;
import com.fuse.ai.server.manager.enums.RunwayAlephAspectRatioEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 视频转换请求参数
 */
@Data
public class RunwayAlephGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指导如何对参考视频进行转换的描述性文本
     */
    @NotBlank(message = "转换提示词不能为空")
    @Size(min = 1, max = 2000, message = "提示词长度必须在1-2000个字符之间")
    private String prompt;

    /**
     * 要进行转换的参考视频的URL
     */
    @NotBlank(message = "视频URL不能为空")
    @URL(message = "视频URL格式不正确")
    @Pattern(regexp = "^https://.*", message = "视频URL必须通过HTTPS访问")
    private String videoUrl;

    /**
     * 可选的webhook URL，用于接收完成通知
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 可选的水印文本
     */
    @Size(max = RunwayVideoAlephConstant.WATERMARK_MAX_LENGTH,
            message = "水印文本长度不能超过" + RunwayVideoAlephConstant.WATERMARK_MAX_LENGTH + "个字符")
    private String waterMark;

    /**
     * 生成视频的存储位置选择
     */
    private Boolean uploadCn = false;

    /**
     * 可选的视频纵横比
     */
    private RunwayAlephAspectRatioEnum aspectRatio;

    /**
     * 可选的随机种子
     */
    @Min(value = 10000, message = "随机种子最小值为10000")
    @Max(value = 999999, message = "随机种子最大值为999999")
    private Integer seed;

    /**
     * 可选的参考图像URL
     */
    @URL(message = "参考图像URL格式不正确")
    private String referenceImage;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 验证视频URL必须以https开头
        if (videoUrl != null && !videoUrl.startsWith("https://")) {
            throw new IllegalArgumentException("视频URL必须通过HTTPS访问");
        }

        // 验证参考图像URL
        if (referenceImage != null && !referenceImage.startsWith("https://")) {
            throw new IllegalArgumentException("参考图像URL必须通过HTTPS访问");
        }

    }

    /**
     * 构建默认请求（用于快速构建）
     */
    public static RunwayAlephGenerateRequest of(String prompt, String videoUrl) {
        RunwayAlephGenerateRequest request = new RunwayAlephGenerateRequest();
        request.setPrompt(prompt);
        request.setVideoUrl(videoUrl);
        return request;
    }

    /**
     * 构建带回调的请求
     */
    public static RunwayAlephGenerateRequest of(String prompt, String videoUrl, String callBackUrl) {
        RunwayAlephGenerateRequest request = of(prompt, videoUrl);
        request.setCallBackUrl(callBackUrl);
        return request;
    }
}