package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum ModelTypeEnum {
    CHAT(1, "聊天"),
    IMAGE(2, "图片"),
    AUDIO(3, "音频"),
    VIDEO(4, "视频");

    private final Integer code;
    private final String description;

    ModelTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}