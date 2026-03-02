package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.Order;
import com.fuse.ai.server.manager.enums.OrderStatusEnum;
import com.fuse.ai.server.manager.enums.OrderTypeEnum;
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

    @Override
    public Integer updateById(Order order) {
        return orderMapper.updateById(order);
    }

    @Override
    public Order selectById(Integer id) {
        return orderMapper.selectById(id);
    }

    @Override
    public Order selectByUserId(Integer userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Order selectByStripePaymentIntentId(String stripePaymentIntentId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStripePaymentIntentId, stripePaymentIntentId);
        queryWrapper.eq(Order::getIsDel, 0);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Order selectByStripeOrderId(String stripeOrderId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStripeOrderId, stripeOrderId);
        queryWrapper.eq(Order::getIsDel, 0);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Order selectByStripeInvoiceId(String stripeInvoiceId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStripeInvoiceId, stripeInvoiceId);
        queryWrapper.eq(Order::getIsDel, 0);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Order selectByUserIdAndType(Integer userId, OrderTypeEnum type) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        queryWrapper.eq(Order::getType, type);
        queryWrapper.eq(Order::getStatus, OrderStatusEnum.PAID);
        queryWrapper.eq(Order::getIsDel, 0);
        queryWrapper.orderByDesc(Order::getGmtModified).last("limit 1");
        return orderMapper.selectOne(queryWrapper);
    }
}