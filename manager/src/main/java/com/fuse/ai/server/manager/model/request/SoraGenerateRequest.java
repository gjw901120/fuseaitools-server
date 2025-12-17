package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SoraModelEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class SoraGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "模型类型不能为空")
    private SoraModelEnum model;

    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    // 根据不同模型使用不同的输入对象
    private Object input;

    /**
     * 验证请求参数
     */
    public void validate() {
        if (model == null) {
            throw new IllegalArgumentException("模型类型不能为空");
        }

        if (input == null) {
            throw new IllegalArgumentException("输入参数不能为空");
        }

        // 根据模型类型验证对应的输入参数
        switch (model) {
            case SORA_2_TEXT_TO_VIDEO:
                if (!(input instanceof SoraTextToVideoRequest)) {
                    throw new IllegalArgumentException("Sora 2 文生视频需要正确的输入参数类型");
                }
                break;
            case SORA_2_IMAGE_TO_VIDEO:
                if (!(input instanceof SoraImageToVideoRequestRequest)) {
                    throw new IllegalArgumentException("Sora 2 图生视频需要正确的输入参数类型");
                }
                break;
            case SORA_2_PRO_TEXT_TO_VIDEO:
                if (!(input instanceof SoraProTextToVideoRequestRequest)) {
                    throw new IllegalArgumentException("Sora 2 Pro 文生视频需要正确的输入参数类型");
                }
                break;
            case SORA_2_PRO_IMAGE_TO_VIDEO:
                if (!(input instanceof SoraProImageToVideoRequestRequest)) {
                    throw new IllegalArgumentException("Sora 2 Pro 图生视频需要正确的输入参数类型");
                }
                break;
            case SORA_WATERMARK_REMOVER:
                if (!(input instanceof SoraWatermarkRemoverRequest)) {
                    throw new IllegalArgumentException("Sora 水印移除需要正确的输入参数类型");
                }
                break;
            case SORA_2_PRO_STORYBOARD:
                if (!(input instanceof SoraStoryboardRequest)) {
                    throw new IllegalArgumentException("Sora 2 Pro 故事板需要正确的输入参数类型");
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的模型类型: " + model);
        }
    }
}