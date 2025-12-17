package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SunoModelEnum;
import com.fuse.ai.server.manager.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 延长音乐请求参数
 */
@Data
public class SunoExtendRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否使用自定义参数
     */
    @NotNull(message = "参数标志不能为空")
    private Boolean defaultParamFlag;

    /**
     * 要延长的音频ID
     */
    @NotBlank(message = "音频ID不能为空")
    private String audioId;

    /**
     * 延长内容的提示词
     */
    @Size(max = 3000, message = "提示词长度不能超过3000个字符")
    private String prompt;

    /**
     * 延长音频的音乐风格
     */
    @Size(max = 200, message = "风格描述长度不能超过200个字符")
    private String style;

    /**
     * 延长音乐曲目的标题
     */
    @Size(max = 80, message = "标题长度不能超过80个字符")
    private String title;

    /**
     * 开始扩展的时间点（秒）
     */
    @Min(value = 1, message = "开始时间必须大于0")
    private Double continueAt;

    /**
     * 用于生成的AI模型版本
     */
    @NotNull(message = "模型版本不能为空")
    private SunoModelEnum model;

    /**
     * 回调URL
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 排除的音乐风格或特征
     */
    private String negativeTags;

    /**
     * 人声性别偏好
     */
    private SunoVocalGenderEnum vocalGender;

    /**
     * 风格遵循强度
     */
    @DecimalMin(value = "0.0", message = "风格权重不能小于0")
    @DecimalMax(value = "1.0", message = "风格权重不能大于1")
    @Digits(integer = 1, fraction = 2, message = "风格权重最多保留两位小数")
    private Double styleWeight;

    /**
     * 实验性偏离程度控制
     */
    @DecimalMin(value = "0.0", message = "创意偏离度不能小于0")
    @DecimalMax(value = "1.0", message = "创意偏离度不能大于1")
    @Digits(integer = 1, fraction = 2, message = "创意偏离度最多保留两位小数")
    private Double weirdnessConstraint;

    /**
     * 音频要素相对权重
     */
    @DecimalMin(value = "0.0", message = "音频权重不能小于0")
    @DecimalMax(value = "1.0", message = "音频权重不能大于1")
    @Digits(integer = 1, fraction = 2, message = "音频权重最多保留两位小数")
    private Double audioWeight;

    /**
     * 人格ID
     */
    private String personaId;

    /**
     * 构建延长请求
     */
    public static SunoExtendRequest build(String audioId, Boolean defaultParamFlag,
                                          SunoModelEnum model, String callBackUrl) {
        SunoExtendRequest request = new SunoExtendRequest();
        request.setAudioId(audioId);
        request.setDefaultParamFlag(defaultParamFlag);
        request.setModel(model);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建完整延长请求
     */
    public static SunoExtendRequest buildFull(String audioId, Boolean defaultParamFlag,
                                              String prompt, String style, String title,
                                              Double continueAt, SunoModelEnum model, String callBackUrl) {
        SunoExtendRequest request = build(audioId, defaultParamFlag, model, callBackUrl);
        request.setPrompt(prompt);
        request.setStyle(style);
        request.setTitle(title);
        request.setContinueAt(continueAt);
        return request;
    }

    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        // 自定义参数模式下的验证
        if (Boolean.TRUE.equals(defaultParamFlag)) {
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义参数模式下提示词不能为空");
            }
            if (style == null || style.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义参数模式下风格不能为空");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义参数模式下标题不能为空");
            }
            if (continueAt == null || continueAt <= 0) {
                throw new IllegalArgumentException("自定义参数模式下开始时间必须大于0");
            }
        }
    }
}