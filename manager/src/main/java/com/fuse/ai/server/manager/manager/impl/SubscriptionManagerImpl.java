package com.fuse.ai.server.manager.manager.impl;

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

}