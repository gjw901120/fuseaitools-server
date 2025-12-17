package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno音频延长请求参数
 */
@Data
public class SunoExtendDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 控制延长时使用的参数来源
     */
    @NotNull(message = "参数来源标志不能为空")
    private Boolean defaultParamFlag;

    /**
     * 要延长的音频曲目的唯一标识符
     */
    @NotBlank(message = "音频ID不能为空")
    private String audioId;

    /**
     * 模型版本
     */
    @NotNull(message = "模型不能为空")
    private SunoModelEnum model;

    /**
     * 延长内容提示词
     */
    @Size(max = 3000, message = "提示词长度不能超过3000个字符")
    private String prompt;

    /**
     * 音乐风格
     */
    @Size(max = 200, message = "风格长度不能超过200个字符")
    private String style;

    /**
     * 音乐标题
     */
    @Size(max = 80, message = "标题长度不能超过80个字符")
    private String title;

    /**
     * 延长开始时间点
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "延长开始时间必须大于0")
    private BigDecimal continueAt;

    /**
     * 排除的音乐风格
     */
    private String negativeTags;

    /**
     * 人声性别偏好
     */
    private SunoVocalGenderEnum vocalGender;

    /**
     * 风格遵循强度
     */
    @DecimalMin(value = "0.0", message = "风格权重最小为0")
    @DecimalMax(value = "1.0", message = "风格权重最大为1")
    private BigDecimal styleWeight;

    /**
     * 创意偏离程度
     */
    @DecimalMin(value = "0.0", message = "创意偏离程度最小为0")
    @DecimalMax(value = "1.0", message = "创意偏离程度最大为1")
    private BigDecimal weirdnessConstraint;

    /**
     * 音频要素权重
     */
    @DecimalMin(value = "0.0", message = "音频权重最小为0")
    @DecimalMax(value = "1.0", message = "音频权重最大为1")
    private BigDecimal audioWeight;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 自定义参数模式下的校验
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
            if (continueAt == null) {
                throw new IllegalArgumentException("自定义参数模式下延长开始时间不能为空");
            }
        }

        // 校验音频ID格式
        if (audioId == null || audioId.trim().isEmpty()) {
            throw new IllegalArgumentException("音频ID不能为空");
        }
    }
}