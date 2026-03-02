package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.OrderRefund;
import com.fuse.ai.server.manager.manager.OrderRefundManager;
import com.fuse.ai.server.manager.mapper.OrderRefundMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderRefundManagerImpl implements OrderRefundManager {

    @Resource
    private OrderRefundMapper orderRefundMapper;

    @Override
    public void insert(OrderRefund orderRefund) {
        orderRefundMapper.insert(orderRefund);
    }

    @Override
    public OrderRefund selectByUuid(String uuid) {
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderRefund::getUuid, uuid);
        queryWrapper.eq(OrderRefund::getIsDel, 0);
        return orderRefundMapper.selectOne(queryWrapper);
    }

    @Override
    public OrderRefund selectByOrderId(Integer orderId) {
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderRefund::getOrderId, orderId);
        queryWrapper.eq(OrderRefund::getIsDel, 0);
        return orderRefundMapper.selectOne(queryWrapper);
    }

    @Override
    public OrderRefund selectByStripeRefundId(String stripeRefundId) {
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderRefund::getStripeRefundId, stripeRefundId);
        queryWrapper.eq(OrderRefund::getIsDel, 0);
        return orderRefundMapper.selectOne(queryWrapper);
    }
    @Override
    public void update(OrderRefund orderRefund) {
        orderRefundMapper.updateById(orderRefund);
    }


}