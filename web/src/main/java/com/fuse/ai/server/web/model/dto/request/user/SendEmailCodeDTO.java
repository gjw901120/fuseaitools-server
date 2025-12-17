package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class SendEmailCodeDTO implements Serializable {



    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;


    @Pattern(regexp = "^(register|login)$", message = "sendType must be 'register' or 'login'")
    private String sendType;

}
