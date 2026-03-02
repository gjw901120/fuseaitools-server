package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.Subscription;
import com.fuse.ai.server.manager.manager.SubscriptionManager;
import com.fuse.ai.server.manager.mapper.SubscriptionMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SubscriptionManagerImpl implements SubscriptionManager {

    @Resource
    private SubscriptionMapper subscriptionMapper;


    @Override
    public Integer insert(Subscription subscription) {
        subscriptionMapper.insert(subscription);
        return subscription.getId();
    }

    @Override
    public Subscription selectById(Integer id) {
        return subscriptionMapper.selectById(id);
    }

    @Override
    public Subscription selectByUserId(Integer userId) {
        LambdaQueryWrapper<Subscription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscription::getUserId, userId);
        queryWrapper.eq(Subscription::getStatus, 1);
        queryWrapper.eq(Subscription::getIsDel, 0);
        queryWrapper.orderByDesc(Subscription::getGmtModified);
        queryWrapper.last("limit 1");
        return subscriptionMapper.selectOne(queryWrapper);
    }
    @Override
    public Subscription selectByOrderId(Integer orderId) {
        LambdaQueryWrapper<Subscription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscription::getOrderId, orderId);
        queryWrapper.eq(Subscription::getStatus, 1);
        queryWrapper.eq(Subscription::getIsDel, 0);
        queryWrapper.orderByDesc(Subscription::getGmtModified);
        queryWrapper.last("limit 1");
        return subscriptionMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer update(Subscription subscription) {
        return subscriptionMapper.updateById(subscription);
    }
}