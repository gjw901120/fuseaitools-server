package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * Suno模型枚举
 */
@Getter
public enum SunoModelEnum {

    V3_5("V3_5", "V3.5 更好的歌曲结构，最长4分钟"),
    V4("V4", "V4 改进的人声质量，最长4分钟"),
    V4_5("V4_5", "V4.5 更智能的提示词，更快的生成速度，最长8分钟"),
    V4_5PLUS("V4_5PLUS", "V4.5+ 音色更丰富，新的创作方式，最长8分钟"),
    V5("V5", "V5 更卓越的音乐表现力，生成速度更快");

    private final String code;
    private final String description;

    SunoModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SunoModelEnum getByCode(String code) {
        for (SunoModelEnum model : values()) {
            if (model.getCode().equals(code)) {
                return model;
            }
        }
        return V3_5;
    }
}