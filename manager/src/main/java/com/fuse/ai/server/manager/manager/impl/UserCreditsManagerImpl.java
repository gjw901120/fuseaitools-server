package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserCredits;
import com.fuse.ai.server.manager.manager.UserCreditsManager;
import com.fuse.ai.server.manager.mapper.UserCreditsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserCreditsManagerImpl implements UserCreditsManager {

    @Resource
    private UserCreditsMapper userCreditsMapper;

    @Override
    public Integer insert(UserCredits userCredits) {
        userCreditsMapper.insert(userCredits);
        return userCredits.getId();
    }

    @Override
    public UserCredits getDetailByUserIdAndType(Integer userId, Integer type) {
        LambdaQueryWrapper<UserCredits> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserCredits::getUserId, userId)
                .eq(UserCredits::getType, type)
                .eq(UserCredits::getStatus,1)
                .eq(UserCredits::getIsDel, 0);
        return userCreditsMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateStatusByUserId(Integer userId, Integer status) {
        LambdaQueryWrapper<UserCredits> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserCredits::getUserId, userId)
                .eq(UserCredits::getIsDel, 0);
        UserCredits userCredits = userCreditsMapper.selectOne(queryWrapper);
        if (userCredits != null) {
            userCredits.setStatus(status);
            userCreditsMapper.updateById(userCredits);
        }
    }

    @Override
    public Integer updateById(UserCredits userCredits) {
        return userCreditsMapper.updateById(userCredits);
    }

}