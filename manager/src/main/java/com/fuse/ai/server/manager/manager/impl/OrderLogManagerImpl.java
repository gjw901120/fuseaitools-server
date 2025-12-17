package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.entity.OrderLog;

import com.fuse.ai.server.manager.manager.OrderLogManager;
import com.fuse.ai.server.manager.mapper.OrderLogMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderLogManagerImpl implements OrderLogManager {

    @Resource
    private OrderLogMapper orderLogMapper;

    @Override
    public Integer insert(OrderLog orderLog) {
        orderLogMapper.insert(orderLog);
        return orderLog.getId();
    }

}