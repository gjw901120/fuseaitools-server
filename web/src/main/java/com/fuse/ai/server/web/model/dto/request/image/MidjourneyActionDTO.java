package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用于执行 Midjourney 按钮动作（如Upscale、Variation等）的请求传输对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时忽略值为null的字段
public class MidjourneyActionDTO {

    /**
     * 是否选择同一频道下的账号
     * true: 使用同一频道下的账号执行动作
     * false: 默认行为，只使用任务关联的原始账号
     */
    @NotNull(message = "Whether to choose the same channel cannot be empty")
    @JsonProperty("chooseSameChannel")
    private Boolean chooseSameChannel;

    /**
     * 动作标识 (必需)
     * 示例值: MJ::JOB::upsample::2::3dbbd469-36af-4a0f-8f02-df6c579e7011
     */
    @NotBlank(message = "The action identifier cannot be empty")
    @JsonProperty("customId")
    private String customId;

    /**
     * 任务ID (必需)
     * 需要执行动作的原始任务ID
     */
    @NotBlank(message = "The task ID cannot be empty")
    @JsonProperty("recordId")
    private String recordId;

}