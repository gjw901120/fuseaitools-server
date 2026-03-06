package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RefreshTimezoneDTO {

    @NotNull(message = "Time zone offset cannot be null")
    private Integer timeZoneOffset;

    @NotBlank(message = "Time zone cannot be null")
    private String timeZone;
}
