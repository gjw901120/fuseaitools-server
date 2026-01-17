package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author system
 * @date 2024/02/15
 */
@Getter
public enum UserRoleEnum {

    /**
     * 用户角色
     */
    USER(1, "user"),

    /**
     * 助手角色
     */
    ASSISTANT(2, "assistant");

    /**
     * 角色编码
     */
    @EnumValue
    private final Integer code;

    /**
     * 角色描述
     */
    private final String description;

    UserRoleEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 角色枚举
     */
    public static UserRoleEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum;
            }
        }
        return null;
    }

    /**
     * 根据描述获取枚举
     *
     * @param description 描述
     * @return 角色枚举
     */
    public static UserRoleEnum getByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.getDescription().equalsIgnoreCase(description.trim())) {
                return roleEnum;
            }
        }
        return null;
    }

    /**
     * 判断编码是否存在
     *
     * @param code 编码
     * @return 是否存在
     */
    public static boolean contains(Integer code) {
        return getByCode(code) != null;
    }

    /**
     * 判断描述是否存在
     *
     * @param description 描述
     * @return 是否存在
     */
    public static boolean contains(String description) {
        return getByDescription(description) != null;
    }

    /**
     * 获取所有编码数组
     *
     * @return 编码数组
     */
    public static Integer[] getAllCodes() {
        UserRoleEnum[] values = values();
        Integer[] codes = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            codes[i] = values[i].getCode();
        }
        return codes;
    }

    /**
     * 获取所有描述数组
     *
     * @return 描述数组
     */
    public static String[] getAllDescriptions() {
        UserRoleEnum[] values = values();
        String[] descriptions = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            descriptions[i] = values[i].getDescription();
        }
        return descriptions;
    }

    /**
     * 转换为字符串
     *
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return this.name() + "(" + code + ":" + description + ")";
    }
}