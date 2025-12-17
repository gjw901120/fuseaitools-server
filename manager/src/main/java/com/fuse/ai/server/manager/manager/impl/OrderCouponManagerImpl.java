package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.OrderCouponManager;
import com.fuse.ai.server.manager.mapper.OrderCouponMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderCouponManagerImpl implements OrderCouponManager {

    @Resource
    private OrderCouponMapper orderCouponMapper;

}
