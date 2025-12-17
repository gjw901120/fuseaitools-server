package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 认证类型枚举（支持MyBatis-Plus和JSON序列化）
 */
@Getter
public enum AuthTypeEnum {
    EMAIL(1, "邮箱认证"),
    GOOGLE(2, "Google认证");

    @EnumValue  // MyBatis-Plus注解：标记数据库存储的值
    @JsonValue  // Jackson注解：序列化时使用code值
    private final int code;

    private final String description;

    AuthTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * JSON反序列化方法（支持从code或name转换）
     */
    @JsonCreator
    public static AuthTypeEnum fromJson(Object value) {
        if (value == null) return null;

        if (value instanceof Integer) {
            return fromCode((Integer) value);
        } else if (value instanceof String) {
            String str = (String) value;
            try {
                // 尝试解析为code
                return fromCode(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                // 如果不是数字，尝试按枚举名解析
                try {
                    return AuthTypeEnum.valueOf(str.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("无效的认证类型: " + str);
                }
            }
        }
        throw new IllegalArgumentException("无效的认证类型值类型: " + value.getClass());
    }

    // 保留原有的fromCode方法
    public static AuthTypeEnum fromCode(int code) {
        for (AuthTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的认证类型code: " + code);
    }
}
