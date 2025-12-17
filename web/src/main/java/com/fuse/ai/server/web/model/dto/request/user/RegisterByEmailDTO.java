package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterByEmailDTO {

    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "The username should be between 2 and 20 characters long")
    private String username;

    @NotBlank(message = "Verification code cannot be empty")
    @Size(min = 6, max = 6, message = "The verification code consists of 6 digits")
    private String code;

    private String timeZone;

}
