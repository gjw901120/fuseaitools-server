package com.fuse.ai.server.web.model.dto.request.video;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Grok Imagine 视频扩展请求 DTO
 * 模型: grok-imagine/extend
 */
@Data
public class GrokImagineExtendDTO {

    /**
     * 模型名称，固定为 grok-imagine/extend
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "grok-imagine-extend", message = "Model must be grok-imagine-extend")
    private String model;

    /**
     * 之前成功的视频生成任务的任务 ID
     * 必须来自 Kie AI 视频生成模型（例如 grok-imagine/text-to-video）
     * 最大长度：100 字符
     */
    @NotBlank(message = "Task ID cannot be empty")
    @Size(max = 100, message = "Task ID cannot exceed 100 characters")
    private String taskId;

    /**
     * 描述所需视频运动的文本提示
     * 详细描述视频如何扩展和延续
     */
    @NotBlank(message = "Prompt cannot be empty")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify "
    )
    private String prompt;

    /**
     * 视频扩展的起点位置
     */
    private BigDecimal extendAt;

    /**
     * 视频扩展的持续时间（秒）
     * 6: 扩展 6 秒视频内容
     * 10: 扩展 10 秒视频内容
     * 默认值：6
     */
    @NotBlank(message = "Extend times cannot be empty")
    @Pattern(regexp = "6|10", message = "Extend times must be 6 or 10")
    private String extendTimes;
}