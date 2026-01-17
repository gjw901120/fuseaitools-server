package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.*;
import com.fuse.ai.server.web.model.dto.response.LoginResponse;
import com.fuse.ai.server.web.service.UserService;
import com.fuse.common.core.entity.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/send-email-code")
    public ResponseResult<?> sendEmailCode(@RequestBody @Valid SendEmailCodeDTO sendEmailCodeDTO, HttpServletRequest request) {

        return ResponseResult.success(userService.sendEmailCode(sendEmailCodeDTO, request));

    }

    @PostMapping("/login-by-email")
    public ResponseResult<?> loginByEmail(@RequestBody @Valid LoginByEmailDTO loginByEmailDTO) {

        return ResponseResult.success(userService.loginByEmail(loginByEmailDTO));

    }

    @GetMapping("/login/google/callback")
    public void loginByGoogle(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {

        log.info("Processing Google login callback: code={}, state={}", code, state);

        LoginResponse loginResponse = userService.loginByGoogle(code);

        String redirectUrl = "http://localhost:3000/home".concat("?token=").concat(loginResponse.getToken());

        log.info("Processing Google login redirectUrl：{} ", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/detail")
    public ResponseResult<?> detail(@AuthenticationPrincipal UserJwtDTO userJwtDTO, @RequestParam String timeZone) {
        //TODO 用户获取详情的时候，刷新timeZone  向上取整
        return ResponseResult.success(userService.detail(userJwtDTO, timeZone));

    }

    @PostMapping("/update")
    public ResponseResult<?> update(@AuthenticationPrincipal UserJwtDTO userJwtDTO, @RequestBody @Valid UpdateUserDTO updateUserDTO) {

        return ResponseResult.success(userService.update(userJwtDTO, updateUserDTO));

    }

}
