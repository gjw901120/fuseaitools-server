package com.fuse.ai.server.manager.manager;


import com.fuse.ai.server.manager.entity.SubscriptionConfig;

public interface SubscriptionConfigManager {

    SubscriptionConfig getDetailByPackage(Integer packageType);

}
