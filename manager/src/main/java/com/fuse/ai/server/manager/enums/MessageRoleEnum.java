package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MessageRoleEnum {
    USER(1, "用户"),
    ASSISTANT(2, "助手");

    private final Integer code;
    private final String description;

    MessageRoleEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}