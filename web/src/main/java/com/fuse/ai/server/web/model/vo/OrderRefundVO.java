package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRefundVO {

    private BigDecimal refundAmount;
}
