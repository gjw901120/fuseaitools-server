package com.fuse.ai.server.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("order_log")
@Accessors(chain = true)
public class OrderLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private String detail;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 创建订单日志对象的便捷方法
     */
    public static OrderLog create(Integer orderId, String detail) {
        return OrderLog.builder()
                .orderId(orderId)
                .detail(detail)
                .build();
    }
}