package com.fuse.ai.server.web.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Flux Kontext模型枚举
 */
@Getter
public enum FluxKontextModelEnum {

    FLUX_KONTEXT_PRO("flux-kontext-pro", "性能平衡的标准模型"),
    FLUX_KONTEXT_MAX("flux-kontext-max", "具有高级功能的增强模型");

    private final String code;
    private final String description;

    FluxKontextModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * JSON序列化时，使用code字段的值
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * JSON反序列化时，从code字符串转换为枚举
     * 注意：方法名必须是 fromCode 或 valueOf
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)  // 添加 mode 参数
    public static FluxKontextModelEnum fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (FluxKontextModelEnum value : values()) {
            if (value.getCode().equalsIgnoreCase(code.trim())) {
                return value;
            }
        }
        return null;
    }

    public static FluxKontextModelEnum fromString(String str) {
        return fromCode(str);
    }
}
