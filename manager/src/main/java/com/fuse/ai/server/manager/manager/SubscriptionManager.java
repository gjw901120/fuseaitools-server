package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.Subscription;

public interface SubscriptionManager {

    Integer insert(Subscription subscription);

    Subscription selectById(Integer id);

    Subscription selectByUserId(Integer userId);

    Subscription selectByOrderId(Integer orderId);

    Integer update(Subscription subscription);

}
