package com.fuse.ai.server.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuse.ai.server.manager.constant.ElevenLabsConstant;
import com.fuse.ai.server.manager.enums.ElevenLabsModelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * 音效生成请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElevenLabsSoundEffectRequest extends ElevenLabsBaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 输入参数
     */
    @NotNull(message = "输入参数不能为空")
    private SoundEffectInput input;

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
        if (modelEnum != ElevenLabsModelEnum.SOUND_EFFECT_V2) {
            throw new IllegalArgumentException("音效生成只支持sound-effect-v2模型");
        }
    }

    /**
     * 构建音效生成请求
     */
    public static ElevenLabsSoundEffectRequest of(String text, String callBackUrl) {
        ElevenLabsSoundEffectRequest request = new ElevenLabsSoundEffectRequest();
        request.setModel(ElevenLabsModelEnum.SOUND_EFFECT_V2.getCode());
        request.setCallBackUrl(callBackUrl);

        SoundEffectInput input = new SoundEffectInput();
        input.setText(text);
        request.setInput(input);

        return request;
    }

    /**
     * 输入参数内部类
     */
    @Data
    public static class SoundEffectInput implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 描述音效的文本
         */
        @NotBlank(message = "音效描述文本不能为空")
        @Size(max = ElevenLabsConstant.TEXT_MAX_LENGTH, message = "音效描述文本长度不能超过" + ElevenLabsConstant.TEXT_MAX_LENGTH + "个字符")
        private String text;

        /**
         * 是否循环播放
         */
        private Boolean loop;

        /**
         * 持续时间（秒）
         */
        @JsonProperty("duration_seconds")
        private Double durationSeconds;

        /**
         * 提示词影响力
         */
        @JsonProperty("prompt_influence")
        private Double promptInfluence;

        /**
         * 输出格式
         */
        @JsonProperty("output_format")
        private String outputFormat;

        /**
         * 业务参数校验
         */
        public void validateBusinessRules() {
            validateRange(durationSeconds, ElevenLabsConstant.MIN_DURATION, ElevenLabsConstant.MAX_DURATION,
                    ElevenLabsConstant.STEP_DURATION, "持续时间");
            validateRange(promptInfluence, ElevenLabsConstant.MIN_PROMPT_INFLUENCE, ElevenLabsConstant.MAX_PROMPT_INFLUENCE,
                    ElevenLabsConstant.STEP_SMALL, "提示词影响力");
        }

        private void validateRange(Double value, Double min, Double max, Double step, String fieldName) {
            if (value != null) {
                if (value < min || value > max) {
                    throw new IllegalArgumentException(fieldName + "必须在" + min + "到" + max + "之间");
                }
                if (step != null && Math.abs(value % step) > 0.001) {
                    throw new IllegalArgumentException(fieldName + "必须是" + step + "的倍数");
                }
            }
        }
    }
}