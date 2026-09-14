package com.fuse.ai.server.web.model.dto.request.order;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefundOrderDTO {

    @NotBlank(message = "refundOrderId cannot be empty")
    private String refundOrderId;

}
