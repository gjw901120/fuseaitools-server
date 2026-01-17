package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.constant.NanoBananaConstant;
import com.fuse.ai.server.manager.enums.NanoBananaAspectRatioEnum;
import com.fuse.ai.server.manager.enums.NanoBananaOutputFormatEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Nano Banana Pro图像生成请求参数
 */
@Data
public class NanoBananaProGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String model = NanoBananaConstant.MODEL_PRO_GENERATE;

    /**
     * 回调URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private Input input;

    @Data
    public static class Input implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 图像生成提示词
         */
        @NotBlank(message = "提示词不能为空")
        @Size(max = NanoBananaConstant.PROMPT_MAX_LENGTH, message = "提示词长度不能超过" + NanoBananaConstant.PROMPT_MAX_LENGTH + "个字符")
        private String prompt;

        @NotNull(message = "输入图像URL列表不能为空")
        @Size(min = 1, max = 8, message = "输入图像数量必须在1到8之间")
        @JsonProperty("image_input")
        private List<@URL(message = "图像URL格式不正确") String> imageInput;

        @NotNull(message = "resolution不能为空")
        @Pattern(regexp = "1K|2K|4K", message = "Resolution must be 1K,2K,4K")
        private String resolution;

        /**
         * 输出格式
         */
        @JsonProperty("output_format")
        private NanoBananaOutputFormatEnum outputFormat = NanoBananaOutputFormatEnum.PNG;

        /**
         * 图像尺寸比例
         */
        @NotNull(message = "图像尺寸比例不能为空")
        @JsonProperty("aspect_ratio")
        private NanoBananaAspectRatioEnum imageSize = NanoBananaAspectRatioEnum.RATIO_1_1;
    }

}