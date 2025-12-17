package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno上传延长请求参数
 */
@Data
public class SunoUploadExtendDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上传的音频文件
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile uploadFile;

    /**
     * 是否启用自定义参数模式
     */
    @NotNull(message = "自定义参数模式不能为空")
    private Boolean defaultParamFlag;

    /**
     * 模型版本
     */
    @NotNull(message = "模型不能为空")
    private SunoModelEnum model;

    /**
     * 是否为纯音乐
     */
    private Boolean instrumental;

    /**
     * 延长内容提示词
     */
    @Size(max = 5000, message = "提示词长度不能超过5000个字符")
    private String prompt;

    /**
     * 音乐风格
     */
    @Size(max = 1000, message = "风格长度不能超过1000个字符")
    private String style;

    /**
     * 音乐标题
     */
    @Size(max = 100, message = "标题长度不能超过100个字符")
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
        // 校验文件
        if (uploadFile == null || uploadFile.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 校验文件类型
        String contentType = uploadFile.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("上传文件必须是音频格式");
        }

        // 校验文件大小
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (uploadFile.getSize() > maxSize) {
            throw new IllegalArgumentException("上传文件大小不能超过10MB");
        }

        // 自定义参数模式下的校验
        if (Boolean.TRUE.equals(defaultParamFlag)) {
            if (style == null || style.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义参数模式下风格不能为空");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("自定义参数模式下标题不能为空");
            }
            if (continueAt == null) {
                throw new IllegalArgumentException("自定义参数模式下延长开始时间不能为空");
            }
            if (Boolean.FALSE.equals(instrumental) && (prompt == null || prompt.trim().isEmpty())) {
                throw new IllegalArgumentException("非纯音乐模式下提示词不能为空");
            }
        }
    }
}