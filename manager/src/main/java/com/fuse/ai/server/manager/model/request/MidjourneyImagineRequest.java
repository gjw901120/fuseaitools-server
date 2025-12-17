package com.fuse.ai.server.manager.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Imagine 任务请求参数
 */
@Data
public class MidjourneyImagineRequest {

    private static final long serialVersionUID = 1L;

    private String botType = "MID_JOURNEY";

    /**
     * 图像生成的文本提示词
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = 2000, message = "提示词长度不能超过2000个字符")
    private String prompt;

    /**
     * 垫图的 base64 编码数组
     */
    @Size(max = 5, message = "垫图数量不能超过5张")
    private List<@NotBlank String> base64Array;

    private String notifyHook;

    private String state;


    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        if (base64Array != null && base64Array.size() > 5) {
            throw new IllegalArgumentException("垫图数量不能超过5张");
        }
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("提示词不能为空");
        }
    }
}