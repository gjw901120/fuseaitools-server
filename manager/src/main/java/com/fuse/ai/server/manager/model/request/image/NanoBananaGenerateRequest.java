package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.constant.NanoBananaConstant;
import com.fuse.ai.server.manager.enums.NanoBananaAspectRatioEnum;
import com.fuse.ai.server.manager.enums.NanoBananaOutputFormatEnum;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.SystemErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * Nano Banana图像生成请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NanoBananaGenerateRequest extends NanoBananaBaseRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private GenerateInput input;

    @Data
    public static class GenerateInput implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 图像生成提示词
         */
        @NotBlank(message = "提示词不能为空")
        @Size(max = NanoBananaConstant.PROMPT_MAX_LENGTH, message = "提示词长度不能超过" + NanoBananaConstant.PROMPT_MAX_LENGTH + "个字符")
        private String prompt;

        /**
         * 输出格式
         */
        @JsonProperty("output_format")
        private NanoBananaOutputFormatEnum outputFormat = NanoBananaOutputFormatEnum.PNG;

        /**
         * 图像尺寸比例
         */
        @NotNull(message = "图像尺寸比例不能为空")
        @JsonProperty("image_size")
        private NanoBananaAspectRatioEnum imageSize = NanoBananaAspectRatioEnum.RATIO_1_1;
    }

    /**
     * 构建基础生成请求
     */
    public static NanoBananaGenerateRequest buildGenerateRequest(String prompt,
                                                                 NanoBananaAspectRatioEnum imageSize,
                                                                 String callBackUrl) {
        NanoBananaGenerateRequest request = new NanoBananaGenerateRequest();
        request.setModel(NanoBananaConstant.MODEL_GENERATE);
        request.setCallBackUrl(callBackUrl);

        GenerateInput input = new GenerateInput();
        input.setPrompt(prompt);
        input.setImageSize(imageSize);
        request.setInput(input);

        return request;
    }

    /**
     * 构建完整生成请求
     */
    public static NanoBananaGenerateRequest buildGenerateRequest(String prompt,
                                                                 NanoBananaAspectRatioEnum imageSize,
                                                                 NanoBananaOutputFormatEnum outputFormat,
                                                                 String callBackUrl) {
        NanoBananaGenerateRequest request = buildGenerateRequest(prompt, imageSize, callBackUrl);
        request.getInput().setOutputFormat(outputFormat);
        return request;
    }

    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        if (input != null && input.getPrompt() != null) {
            // 验证提示词长度
            if (input.getPrompt().length() > NanoBananaConstant.PROMPT_MAX_LENGTH) {
                throw new BaseException(SystemErrorType.SYSTEM_EXECUTION_ERROR, "The prompt word length exceeds the limit.");
            }
        }
    }
}