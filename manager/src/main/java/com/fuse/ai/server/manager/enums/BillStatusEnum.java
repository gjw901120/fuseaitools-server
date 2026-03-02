package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BillStatusEnum {
    PROGRESS(1, "Progress"),
    COMPLETED(2, "Completed"),
    FAILED(3, "Failed");

    @EnumValue
    private final Integer code;
    private final String description;

    BillStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BillStatusEnum of(Integer code) {
        for (BillStatusEnum value : BillStatusEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return PROGRESS;
    }

}
