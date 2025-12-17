package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.entity.Order;
import com.fuse.ai.server.manager.manager.OrderManager;
import com.fuse.ai.server.manager.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderManagerImpl implements OrderManager {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Integer insert(Order order) {
        orderMapper.insert(order);
        return order.getId();
    }

}