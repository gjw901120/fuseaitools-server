package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.constant.ElevenLabsConstant;
import com.fuse.ai.server.manager.enums.ElevenLabsModelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * 语音转文本请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElevenLabsSTTRequest extends ElevenLabsBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private STTInput input;

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
        if (modelEnum != ElevenLabsModelEnum.SPEECH_TO_TEXT) {
            throw new IllegalArgumentException("语音转文本只支持speech-to-text模型");
        }
    }

    /**
     * 构建语音转文本请求
     */
    public static ElevenLabsSTTRequest of(String audioUrl, String callBackUrl) {
        ElevenLabsSTTRequest request = new ElevenLabsSTTRequest();
        request.setModel(ElevenLabsModelEnum.SPEECH_TO_TEXT.getCode());
        request.setCallBackUrl(callBackUrl);

        STTInput input = new STTInput();
        input.setAudioUrl(audioUrl);
        request.setInput(input);

        return request;
    }

    /**
     * 输入参数内部类
     */
    @Data
    public static class STTInput implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 音频文件URL
         */
        @NotBlank(message = "音频URL不能为空")
        @URL(message = "音频URL格式不正确")
        @JsonProperty("audio_url")
        private String audioUrl;

        /**
         * 语言代码
         */
        @Size(max = ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH, message = "语言代码长度不能超过" + ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH + "个字符")
        @JsonProperty("language_code")
        private String languageCode;

        /**
         * 是否标记音频事件
         */
        @JsonProperty("tag_audio_events")
        private Boolean tagAudioEvents;

        /**
         * 是否标注说话人
         */
        private Boolean diarize;

        /**
         * 业务参数校验
         */
        public void validateBusinessRules() {
            validateTextLength(languageCode, ElevenLabsConstant.LANGUAGE_CODE_MAX_LENGTH, "语言代码");
        }

        private void validateTextLength(String text, int maxLength, String fieldName) {
            if (text != null && text.length() > maxLength) {
                throw new IllegalArgumentException(fieldName + "长度不能超过" + maxLength + "个字符");
            }
        }
    }
}