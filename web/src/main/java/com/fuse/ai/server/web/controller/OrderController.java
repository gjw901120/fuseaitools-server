package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.service.OrderService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/refresh-plan")
    public ResponseResult<?> refreshPlan() {
        return ResponseResult.success(orderService.refreshPlan());
    }

}
