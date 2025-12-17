// MidjourneyStatusEnum.java
package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum MidjourneyStatusEnum {
    NOT_START("NOT_START", "未开始"),
    SUBMITTED("SUBMITTED", "已提交"),
    MODAL("MODAL", "模态操作中"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    FAILURE("FAILURE", "失败"),
    SUCCESS("SUCCESS", "成功"),
    CANCEL("CANCEL", "已取消");

    private final String code;
    private final String description;

    MidjourneyStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MidjourneyStatusEnum getByCode(String code) {
        for (MidjourneyStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}