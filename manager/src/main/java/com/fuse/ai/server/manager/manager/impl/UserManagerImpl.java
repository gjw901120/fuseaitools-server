package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.User;
import com.fuse.ai.server.manager.manager.UserManager;
import com.fuse.ai.server.manager.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserManagerImpl implements UserManager {

    @Resource
    private UserMapper userMapper;

    @Override
    public Integer insert(User user) {
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Integer updateById(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public User selectByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getEmail, email)
                .eq(User::getIsDel, 0);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User selectByThirdPartyId(String thirdPartyId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getThirdPartyId, thirdPartyId)
                .eq(User::getIsDel, 0);
        return userMapper.selectOne(queryWrapper);
    }

}