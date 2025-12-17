package com.fuse.ai.server.web.model.dto.request.suno;

import com.fuse.ai.server.web.common.enums.SunoModelEnum;
import com.fuse.ai.server.web.common.enums.SunoVocalGenderEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Suno添加人声请求参数
 */
@Data
public class SunoAddVocalsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 生成音频的提示词
     */
    @NotBlank(message = "提示词不能为空")
    private String prompt;

    /**
     * 音乐标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 排除的音乐风格
     */
    @NotBlank(message = "排除风格不能为空")
    private String negativeTags;

    /**
     * 音乐风格
     */
    @NotBlank(message = "风格不能为空")
    private String style;

    /**
     * 上传的音频文件
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile uploadFile;

    /**
     * 模型版本
     */
    @NotNull(message = "模型不能为空")
    private SunoModelEnum model = SunoModelEnum.V4_5PLUS;

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

        // 校验模型限制
        if (model != SunoModelEnum.V4_5PLUS && model != SunoModelEnum.V5) {
            throw new IllegalArgumentException("添加人声只支持V4_5PLUS和V5模型");
        }
    }
}