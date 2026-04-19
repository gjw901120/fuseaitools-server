package com.fuse.ai.server.manager.model.request.video;

import com.fuse.ai.server.manager.enums.SoraModelEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SoraGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private SoraModelEnum model;

    private String callBackUrl;

    // 根据不同模型使用不同的输入对象
    private Object input;

    /**
     * 验证请求参数
     */
    public void validate() {
        if (model == null) {
            throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "The model type cannot be left blank");
        }

        if (input == null) {
            throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Input parameters cannot be empty.");
        }

        // 根据模型类型验证对应的输入参数
        switch (model) {
            case SORA_2_TEXT_TO_VIDEO:
                if (!(input instanceof SoraTextToVideoRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora 2 Text to Video requires the correct input parameter type");
                }
                break;
            case SORA_2_IMAGE_TO_VIDEO:
                if (!(input instanceof SoraImageToVideoRequestRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora 2 Image to Video requires the correct input parameter type");
                }
                break;
            case SORA_2_PRO_TEXT_TO_VIDEO:
                if (!(input instanceof SoraProTextToVideoRequestRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora 2 Pro Text to Video requires the correct input parameter type");
                }
                break;
            case SORA_2_PRO_IMAGE_TO_VIDEO:
                if (!(input instanceof SoraProImageToVideoRequestRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora 2 Pro Image to Video requires the correct input parameter type");
                }
                break;
            case SORA_WATERMARK_REMOVER:
                if (!(input instanceof SoraWatermarkRemoverRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora Watermark Remover requires the correct input parameter type");
                }
                break;
            case SORA_2_PRO_STORYBOARD:
                if (!(input instanceof SoraStoryboardRequest)) {
                    throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Sora 2 Pro Storyboard requires the correct input parameter type");
                }
                break;
            default:
                throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "Unsupported model type: " + model);
        }
    }
}