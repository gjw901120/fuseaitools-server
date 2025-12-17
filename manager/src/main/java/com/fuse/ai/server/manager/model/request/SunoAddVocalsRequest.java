package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SunoModelEnum;
import com.fuse.ai.server.manager.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 添加人声生成音乐请求参数
 */
@Data
public class SunoAddVocalsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 生成音频的提示词
     */
    @NotBlank(message = "提示词不能为空")
    private String prompt;

    /**
     * 用于生成的AI模型版本
     */
    private SunoModelEnum model = SunoModelEnum.V4_5PLUS;

    /**
     * 音乐的标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 排除的音乐风格
     */
    @NotBlank(message = "排除标签不能为空")
    private String negativeTags;

    /**
     * 音乐的风格
     */
    @NotBlank(message = "风格不能为空")
    private String style;

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
     * 上传的音频文件URL
     */
    @NotBlank(message = "上传URL不能为空")
    @URL(message = "上传URL格式不正确")
    private String uploadUrl;

    /**
     * 回调URL
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 构建添加人声请求
     */
    public static SunoAddVocalsRequest build(String prompt, String title, String negativeTags,
                                             String style, String uploadUrl, String callBackUrl) {
        SunoAddVocalsRequest request = new SunoAddVocalsRequest();
        request.setPrompt(prompt);
        request.setTitle(title);
        request.setNegativeTags(negativeTags);
        request.setStyle(style);
        request.setUploadUrl(uploadUrl);
        request.setCallBackUrl(callBackUrl);
        return request;
    }
}