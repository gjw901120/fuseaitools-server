// VeoExtendRequest.java
package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.VeoVideoExtendConstant;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Veo视频扩展请求参数
 */
@Data
public class VeoExtendRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 原始视频生成任务的ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 描述扩展视频内容的文本提示词
     */
    @NotBlank(message = "扩展提示词不能为空")
    @Size(max = 2000, message = "扩展提示词长度不能超过2000个字符")
    private String prompt;

    /**
     * 随机种子参数
     */
    @Min(value = VeoVideoExtendConstant.SEED_MIN, message = "随机种子最小值为" + VeoVideoExtendConstant.SEED_MIN)
    @Max(value = VeoVideoExtendConstant.SEED_MAX, message = "随机种子最大值为" + VeoVideoExtendConstant.SEED_MAX)
    private Integer seeds;

    /**
     * 水印文本
     */
    @Size(max = 50, message = "水印文本长度不能超过50个字符")
    private String watermark;

    /**
     * 任务完成时的回调URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 可以添加特定的业务规则校验
        // 例如：验证任务ID的格式等
        validateTaskIdFormat();
    }

    /**
     * 验证任务ID格式
     */
    private void validateTaskIdFormat() {
        if (taskId != null && !taskId.startsWith("veo_task_")) {
            throw new IllegalArgumentException("任务ID必须以'veo_task_'开头");
        }
    }

    /**
     * 构建基础扩展请求
     */
    public static VeoExtendRequest of(String taskId, String prompt) {
        VeoExtendRequest request = new VeoExtendRequest();
        request.setTaskId(taskId);
        request.setPrompt(prompt);
        return request;
    }

    /**
     * 构建完整扩展请求
     */
    public static VeoExtendRequest of(String taskId, String prompt, String callBackUrl) {
        VeoExtendRequest request = of(taskId, prompt);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建带种子的扩展请求
     */
    public static VeoExtendRequest of(String taskId, String prompt, Integer seeds, String callBackUrl) {
        VeoExtendRequest request = of(taskId, prompt, callBackUrl);
        request.setSeeds(seeds);
        return request;
    }
}