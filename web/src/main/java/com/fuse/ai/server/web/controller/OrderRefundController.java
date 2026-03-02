package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.OrderService;
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

@RestController
@RequestMapping("/api/refund")
public class OrderRefundController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/confirm-refund-recharge")
    public ResponseResult<?> confirmRefundRecharge(@RequestBody String refundOrderId) {
        try {
            return ResponseResult.success(stripeService.confirmRefundRecharge(refundOrderId));
        } catch (StripeException e) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/confirm-refund-subscription")
    public ResponseResult<?> confirmRefundSubscription(@RequestBody String refundOrderId) {
        try {
            return ResponseResult.success(stripeService.confirmRefundSubscription(refundOrderId));
        } catch (StripeException e) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/cancel-subscription")
    public ResponseResult<?> cancelSubscription(@AuthenticationPrincipal UserJwtDTO userJwtDTO) throws StripeException {
        return ResponseResult.success(stripeService.cancelSubscription(userJwtDTO.getId()));
    }

    @PostMapping("/refund-recharge")
    public ResponseResult<?> refundRecharge(@AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(orderService.refundRecharge(userJwtDTO.getId()));
    }

    @PostMapping("/refund-subscription")
    public ResponseResult<?> refundSubscription(@AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(orderService.refundSubscription(userJwtDTO.getId()));
    }



}
