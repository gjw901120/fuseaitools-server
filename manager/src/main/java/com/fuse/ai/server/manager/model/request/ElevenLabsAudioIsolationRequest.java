package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.enums.ElevenLabsModelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * 音频分离请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElevenLabsAudioIsolationRequest extends ElevenLabsBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private AudioIsolationInput input;

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
        if (modelEnum != ElevenLabsModelEnum.AUDIO_ISOLATION) {
            throw new IllegalArgumentException("音频分离只支持audio-isolation模型");
        }
    }

    /**
     * 构建音频分离请求
     */
    public static ElevenLabsAudioIsolationRequest of(String audioUrl, String callBackUrl) {
        ElevenLabsAudioIsolationRequest request = new ElevenLabsAudioIsolationRequest();
        request.setModel(ElevenLabsModelEnum.AUDIO_ISOLATION.getCode());
        request.setCallBackUrl(callBackUrl);

        AudioIsolationInput input = new AudioIsolationInput();
        input.setAudioUrl(audioUrl);
        request.setInput(input);

        return request;
    }

    /**
     * 输入参数内部类
     */
    @Data
    public static class AudioIsolationInput implements Serializable {

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
         * 业务参数校验
         */
        public void validateBusinessRules() {
            // 可以添加音频URL的额外验证
        }
    }
}