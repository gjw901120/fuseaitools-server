package com.fuse.ai.server.web.model.dto.request.stripe;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CreateSessionDTO {

    @NotNull(message = "Type must not be null")
    @Pattern(regexp = "^(subscription|recharge)$", message = "Type must be either 'subscription' or 'recharge'")
    private String type;

    @NotNull(message = "Price ID must not be null")
    private Integer priceId;
}
