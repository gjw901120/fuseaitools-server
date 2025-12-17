package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginByGoogleDTO {

    @NotBlank(message = "authorizationCode cannot be empty")
    private String authorizationCode;

    private String timeZone;

}
