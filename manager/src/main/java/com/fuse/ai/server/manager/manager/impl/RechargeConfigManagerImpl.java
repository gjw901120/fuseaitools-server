package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.RechargeConfig;
import com.fuse.ai.server.manager.manager.RechargeConfigManager;
import com.fuse.ai.server.manager.mapper.RechargeConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RechargeConfigManagerImpl implements RechargeConfigManager {

    @Autowired
    private RechargeConfigMapper rechargeConfigMapper;

    @Override
    public RechargeConfig getDetailById(Integer id) {
        LambdaQueryWrapper<RechargeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(RechargeConfig::getId, id)
                .eq(RechargeConfig::getIsDel, 0);
        return rechargeConfigMapper.selectOne(queryWrapper);
    }
}
