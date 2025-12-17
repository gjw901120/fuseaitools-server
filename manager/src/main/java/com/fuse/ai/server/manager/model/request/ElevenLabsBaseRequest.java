package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.ElevenLabsModelEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * ElevenLabs基础请求参数
 */
@Data
public class ElevenLabsBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String model;

    /**
     * 回调URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 验证数值范围
     */
    protected void validateRange(Double value, Double min, Double max, Double step, String fieldName) {
        if (value != null) {
            if (value < min || value > max) {
                throw new IllegalArgumentException(fieldName + "必须在" + min + "到" + max + "之间");
            }
            if (step != null && Math.abs(value % step) > 0.001) {
                throw new IllegalArgumentException(fieldName + "必须是" + step + "的倍数");
            }
        }
    }

    /**
     * 验证文本长度
     */
    protected void validateTextLength(String text, int maxLength, String fieldName) {
        if (text != null && text.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + "长度不能超过" + maxLength + "个字符");
        }
    }

    /**
     * 获取模型枚举
     */
    protected ElevenLabsModelEnum getModelEnum() {
        return ElevenLabsModelEnum.getByCode(model);
    }
}