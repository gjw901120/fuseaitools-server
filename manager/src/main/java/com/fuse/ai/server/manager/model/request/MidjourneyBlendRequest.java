package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.MidjourneyConstant;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Blend 任务请求参数
 */
@Data
public class MidjourneyBlendRequest {

    private static final long serialVersionUID = 1L;

    private String botType = "MID_JOURNEY";

    /**
     * 要混合的图片 base64 编码数组
     */
    @NotNull(message = "图片数组不能为空")
    @Size(min = MidjourneyConstant.MIN_BLEND_IMAGES, max = MidjourneyConstant.MAX_BLEND_IMAGES,
            message = "图片数量必须在" + MidjourneyConstant.MIN_BLEND_IMAGES + "到" + MidjourneyConstant.MAX_BLEND_IMAGES + "之间")
    private List<@NotBlank String> base64Array;

    /**
     * 输出图片的宽高比设置
     */
    private Dimensions dimensions = Dimensions.SQUARE;

    private String notifyHook;

    private String state;

    /**
     * 图片尺寸枚举（内部枚举）
     */
    @Getter
    public enum Dimensions {
        PORTRAIT("PORTRAIT", "2:3 比例", "竖版"),
        SQUARE("SQUARE", "1:1 比例", "正方形"),
        LANDSCAPE("LANDSCAPE", "3:2 比例", "横版");

        private final String code;
        private final String description;
        private final String type;

        Dimensions(String code, String description, String type) {
            this.code = code;
            this.description = description;
            this.type = type;
        }

        public static Dimensions getByCode(String code) {
            for (Dimensions value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 验证业务规则
     */
    public void validateBusinessRules() {
        if (base64Array == null || base64Array.size() < MidjourneyConstant.MIN_BLEND_IMAGES ||
                base64Array.size() > MidjourneyConstant.MAX_BLEND_IMAGES) {
            throw new IllegalArgumentException("图片数量必须在" + MidjourneyConstant.MIN_BLEND_IMAGES +
                    "到" + MidjourneyConstant.MAX_BLEND_IMAGES + "之间");
        }
    }
}