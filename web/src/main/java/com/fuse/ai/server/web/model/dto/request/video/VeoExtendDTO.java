package com.fuse.ai.server.web.model.dto.request.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Veo扩展视频生成请求参数
 */
@Data
public class VeoExtendDTO {

    /**
     * 原始视频生成任务的ID
     * 必须是从视频生成接口返回的有效taskId
     * 注意：生成1080P之后的视频不能扩展
     */
    @NotBlank(message = "Task ID cannot be empty")
    private String taskId;

    /**
     * 描述扩展视频内容的文本提示词
     * 应该详细描述您希望视频如何扩展，包括动作、场景变化、风格等
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 1, max = 1000, message = "Prompt length must be between 1-1000 characters")
    private String prompt;

    /**
     * 随机种子参数，用于控制生成内容的随机性
     * 取值范围：10000-99999
     * 相同的种子会生成相似的视频内容，不同的种子会生成不同的视频内容
     * 不填写时系统自动分配
     */
    @Min(value = 10000, message = "Random seed minimum value is 10000")
    @Max(value = 99999, message = "Random seed maximum value is 99999")
    private Integer seeds;

    /**
     * 水印文本（可选）
     * 如果提供，将在生成的视频上添加水印
     */
    @Size(max = 50, message = "Watermark text cannot exceed 50 characters")
    private String watermark;
}