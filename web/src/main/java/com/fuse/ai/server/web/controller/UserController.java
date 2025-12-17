package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.*;
import com.fuse.ai.server.web.service.UserService;
import com.simply.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/send-email-code")
    public ResponseResult<?> sendEmailCode(@RequestBody @Valid SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request) {

        return ResponseResult.success(userService.sendEmailCode(sendEmailCodeDTO, request));

    }

    @PostMapping("/register-by-email")
    public ResponseResult<?> registerByEmail(@RequestBody @Valid RegisterByEmailDTO registerByEmailDTO) {

        return ResponseResult.success(userService.registerByEmail(registerByEmailDTO));

    }

    @PostMapping("/login-by-email")
    public ResponseResult<?> loginByEmail(@RequestBody @Valid LoginByEmailDTO loginByEmailDTO) {

        return ResponseResult.success(userService.loginByEmail(loginByEmailDTO));

    }

    @PostMapping("/login-by-google")
    public ResponseResult<?> loginByGoogle(@RequestBody @Valid LoginByGoogleDTO loginByGoogleDTO) {

        return ResponseResult.success(userService.loginByGoogle(loginByGoogleDTO));

    }

    @GetMapping("/detail")
    public ResponseResult<?> detail(@AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(userService.detail(userJwtDTO));

    }

    @PostMapping("/update")
    public ResponseResult<?> update(@AuthenticationPrincipal UserJwtDTO userJwtDTO, @RequestBody @Valid UpdateUserDTO updateUserDTO) {

        return ResponseResult.success(userService.update(userJwtDTO, updateUserDTO));

    }

}
