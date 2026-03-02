package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.stripe.CreateSessionDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.StripeService;
import com.fuse.common.core.entity.vo.ResponseResult;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-session")
    public ResponseResult<?> createSession(@Valid @RequestBody CreateSessionDTO createSessionDTO, @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        try {
            return ResponseResult.success(stripeService.createSession(createSessionDTO, userJwtDTO.getId()));
        } catch (StripeException e) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, e.getMessage());
        }
    }


}
