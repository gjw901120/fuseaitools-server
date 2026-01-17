package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.constant.ElevenLabsConstant;
import com.fuse.ai.server.manager.enums.ElevenLabsModelEnum;
import com.fuse.ai.server.manager.enums.ElevenLabsVoiceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * 文本转语音请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElevenLabsTTSRequest extends ElevenLabsBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private TTSInput input;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        if (input == null) {
            throw new IllegalArgumentException("输入参数不能为空");
        }

        input.validateBusinessRules();

        // 验证模型支持
        ElevenLabsModelEnum modelEnum = getModelEnum();
        if (modelEnum != ElevenLabsModelEnum.TTS_MULTILINGUAL_V2 &&
                modelEnum != ElevenLabsModelEnum.TTS_TURBO_2_5) {
            throw new IllegalArgumentException("文本转语音只支持TTS模型");
        }
    }

    /**
     * 构建文本转语音请求
     */
    public static ElevenLabsTTSRequest of(String model, String text, ElevenLabsVoiceEnum voice, String callBackUrl) {
        ElevenLabsTTSRequest request = new ElevenLabsTTSRequest();
        request.setModel(model);
        request.setCallBackUrl(callBackUrl);

        TTSInput input = new TTSInput();
        input.setText(text);
        input.setVoice(voice != null ? voice.getCode() : null);
        request.setInput(input);

        return request;
    }

    /**
     * 输入参数内部类
     */
    @Data
    public static class TTSInput implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 要转换为语音的文本
         */
        @NotBlank(message = "文本不能为空")
        @Size(max = ElevenLabsConstant.TEXT_MAX_LENGTH, message = "文本长度不能超过" + ElevenLabsConstant.TEXT_MAX_LENGTH + "个字符")
        private String text;

        /**
         * 语音类型
         */
        private String voice;

        /**
         * 语音稳定性 (0-1)
         */
        private Double stability;

        /**
         * 相似度提升 (0-1)
         */
        @JsonProperty("similarity_boost")
        private Double similarityBoost;

        /**
         * 风格夸张程度 (0-1)
         */
        private Double style;

        /**
         * 语速 (0.7-1.2)
         */
        private Double speed;

        /**
         * 是否返回时间戳
         */
        private Boolean timestamps;

        /**
         * 前文内容
         */
        @Size(max = ElevenLabsConstant.TEXT_MAX_LENGTH, message = "前文长度不能超过" + ElevenLabsConstant.TEXT_MAX_LENGTH + "个字符")
        @JsonProperty("previous_text")
        private String previousText;

        /**
         * 后文内容
         */
        @Size(max = ElevenLabsConstant.TEXT_MAX_LENGTH, message = "后文长度不能超过" + ElevenLabsConstant.TEXT_MAX_LENGTH + "个字符")
        @JsonProperty("next_text")
        private String nextText;

        /**
         * 语言代码
         */
        @Size(max = ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH, message = "语言代码长度不能超过" + ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH + "个字符")
        @JsonProperty("language_code")
        private String languageCode;

        /**
         * 业务参数校验
         */
        public void validateBusinessRules() {
            validateRange(stability, ElevenLabsConstant.MIN_STABILITY, ElevenLabsConstant.MAX_STABILITY,
                    "语音稳定性");
            validateRange(similarityBoost, ElevenLabsConstant.MIN_SIMILARITY_BOOST, ElevenLabsConstant.MAX_SIMILARITY_BOOST,
                    "相似度提升");
            validateRange(style, ElevenLabsConstant.MIN_STYLE, ElevenLabsConstant.MAX_STYLE,
                    "风格夸张程度");
            validateRange(speed, ElevenLabsConstant.MIN_SPEED, ElevenLabsConstant.MAX_SPEED,
                    "语速");

            validateTextLength(previousText, ElevenLabsConstant.TEXT_MAX_LENGTH, "前文");
            validateTextLength(nextText, ElevenLabsConstant.TEXT_MAX_LENGTH, "后文");
            validateTextLength(languageCode, ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH, "语言代码");
        }

        private void validateRange(Double value, Double min, Double max, String fieldName) {
            if (value != null) {
                if (value < min || value > max) {
                    throw new IllegalArgumentException(fieldName + "必须在" + min + "到" + max + "之间");
                }
                if (Math.abs(value % (Double) ElevenLabsConstant.STEP_SMALL) > 0.001) {
                    throw new IllegalArgumentException(fieldName + "必须是" + ElevenLabsConstant.STEP_SMALL + "的倍数");
                }
            }
        }

        private void validateTextLength(String text, int maxLength, String fieldName) {
            if (text != null && text.length() > maxLength) {
                throw new IllegalArgumentException(fieldName + "长度不能超过" + maxLength + "个字符");
            }
        }
    }
}