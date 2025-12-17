package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.RunwayVideoConstant;
import com.fuse.ai.server.manager.enums.RunwayVideoQualityEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Runway视频续集生成请求参数
 */
@Data
public class RunwayExtendRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 原始视频生成任务的唯一标识符
     * 必须是来自先前生成视频的有效任务ID
     */
    @NotBlank(message = "任务ID不能为空")
    @Size(max = 100, message = "任务ID长度不能超过100个字符")
    private String taskId;

    /**
     * 指导视频续集的描述性文本
     * 解释接下来应该发生什么动作、动态或发展
     * 要具体但保持与原始视频内容的一致性
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = RunwayVideoConstant.PROMPT_MAX_LENGTH, message = "提示词长度不能超过" + RunwayVideoConstant.PROMPT_MAX_LENGTH + "个字符")
    private String prompt;

    /**
     * 视频分辨率
     * 可选值为720p或1080p
     */
    @NotNull(message = "视频质量不能为空")
    private RunwayVideoQualityEnum quality;

    /**
     * 视频水印文本内容
     * 空字符串表示不添加水印，非空字符串将在视频右下角显示指定的水印文本
     */
    @Size(max = 50, message = "水印文本长度不能超过50个字符")
    private String waterMark;

    /**
     * 回调URL地址
     * 用于接收AI视频扩展任务完成更新的URL地址
     * 所有视频扩展请求都需要此参数
     */
    @NotBlank(message = "回调URL不能为空")
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;


}