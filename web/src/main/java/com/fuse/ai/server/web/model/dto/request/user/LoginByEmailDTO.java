package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginByEmailDTO {

    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Verification code cannot be empty")
    @Size(min = 6, max = 6, message = "The verification code consists of 6 digits")
    private String code;

    @NotBlank(message = "Request failed. Please try again later!")
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "Request failed. Please try again later!")
    private String deviceId;

}
