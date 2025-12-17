package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SunoModelEnum;
import com.fuse.ai.server.manager.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 上传并翻唱音乐请求参数
 */
@Data
public class SunoUploadCoverRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上传音频文件的URL
     */
    @NotBlank(message = "上传URL不能为空")
    @URL(message = "上传URL格式不正确")
    private String uploadUrl;

    /**
     * 描述期望生成的音频内容
     */
    @Size(max = 5000, message = "提示词长度不能超过5000个字符")
    private String prompt;

    /**
     * 音乐风格或流派
     */
    @Size(max = 1000, message = "风格描述长度不能超过1000个字符")
    private String style;

    /**
     * 生成音乐的标题
     */
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    /**
     * 是否启用自定义模式
     */
    @NotNull(message = "自定义模式不能为空")
    private Boolean customMode;

    /**
     * 是否为纯音乐
     */
    @NotNull(message = "是否为纯音乐不能为空")
    private Boolean instrumental;

    /**
     * 用于生成的AI模型版本
     */
    @NotNull(message = "模型版本不能为空")
    private SunoModelEnum model;

    /**
     * 排除的音乐风格或特征
     */
    private String negativeTags;

    /**
     * 回调URL
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

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
     * 构建上传翻唱请求
     */
    public static SunoUploadCoverRequest build(String uploadUrl, Boolean customMode,
                                               Boolean instrumental, SunoModelEnum model,
                                               String callBackUrl) {
        SunoUploadCoverRequest request = new SunoUploadCoverRequest();
        request.setUploadUrl(uploadUrl);
        request.setCustomMode(customMode);
        request.setInstrumental(instrumental);
        request.setModel(model);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建完整上传翻唱请求
     */
    public static SunoUploadCoverRequest buildFull(String uploadUrl, String prompt, String style,
                                                   String title, Boolean customMode,
                                                   Boolean instrumental, SunoModelEnum model,
                                                   String callBackUrl) {
        SunoUploadCoverRequest request = build(uploadUrl, customMode, instrumental, model, callBackUrl);
        request.setPrompt(prompt);
        request.setStyle(style);
        request.setTitle(title);
        return request;
    }

    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        // 自定义模式下的验证
        if (Boolean.TRUE.equals(customMode)) {
            if (style == null || style.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义模式下风格不能为空");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义模式下标题不能为空");
            }
            if (Boolean.FALSE.equals(instrumental) && (prompt == null || prompt.trim().isEmpty())) {
                throw new IllegalArgumentException("非纯音乐模式下提示词不能为空");
            }
        } else {
            // 非自定义模式下只需要prompt
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("非自定义模式下提示词不能为空");
            }
        }
    }
}