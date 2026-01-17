package com.fuse.ai.server.manager.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BillStatusEnum {
    PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成"),
    FAILED(3, "失败");

    @EnumValue
    private final Integer code;
    private final String description;

    BillStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

}
