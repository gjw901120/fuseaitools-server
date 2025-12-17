package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.RunwayVideoConstant;
import com.fuse.ai.server.manager.enums.RunwayAspectRatioEnum;
import com.fuse.ai.server.manager.enums.RunwayVideoQualityEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * AI视频生成请求参数
 */
@Data
public class RunwayGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指导AI视频生成的描述性文本
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = RunwayVideoConstant.PROMPT_MAX_LENGTH, message = "提示词长度不能超过" + RunwayVideoConstant.PROMPT_MAX_LENGTH + "个字符")
    private String prompt;

    /**
     * 视频时长
     */
    @NotNull(message = "视频时长不能为空")
    @Min(value = RunwayVideoConstant.DURATION_5S, message = "视频时长必须为5或10秒")
    @Max(value = RunwayVideoConstant.DURATION_10S, message = "视频时长必须为5或10秒")
    private Integer duration;

    /**
     * 视频分辨率
     */
    @NotBlank(message = "视频质量不能为空")
    private RunwayVideoQualityEnum quality;

    /**
     * 可选的参考图像URL
     */
    @URL(message = "图片URL格式不正确")
    private String imageUrl;

    /**
     * 视频宽高比
     */
    private RunwayAspectRatioEnum aspectRatio;

    /**
     * 视频水印文本内容
     */
    private String waterMark;

    /**
     * 回调URL地址
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 校验1080p分辨率不能生成10秒视频
        if (RunwayVideoQualityEnum.P_1080.equals(quality) && RunwayVideoConstant.DURATION_10S == duration) {
            throw new IllegalArgumentException("1080p分辨率不能生成10秒视频");
        }

        // 如果有图片URL，则宽高比由图片决定，忽略传入的aspectRatio
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            this.aspectRatio = null;
        } else {
            // 文本生成视频必须指定宽高比
            if (aspectRatio == null) {
                throw new IllegalArgumentException("文本生成视频必须指定宽高比");
            }
        }
    }
}