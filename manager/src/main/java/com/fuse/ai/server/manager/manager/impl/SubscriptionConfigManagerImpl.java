package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.SubscriptionConfig;
import com.fuse.ai.server.manager.manager.SubscriptionConfigManager;
import com.fuse.ai.server.manager.mapper.SubscriptionConfigMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SubscriptionConfigManagerImpl implements SubscriptionConfigManager {

    @Resource
    private SubscriptionConfigMapper subscriptionConfigMapper;


    @Override
    public SubscriptionConfig getDetailByPackage(Integer packageType) {
        LambdaQueryWrapper<SubscriptionConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SubscriptionConfig::getSubscriptionPackage, packageType)
                .eq(SubscriptionConfig::getIsDel, 0);
        return subscriptionConfigMapper.selectOne(queryWrapper);
    }
}