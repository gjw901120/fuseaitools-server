package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.User;
import com.fuse.ai.server.manager.manager.UserManager;
import com.fuse.ai.server.manager.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
    public User selectByUuid(String uuid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUuid, uuid)
                .eq(User::getIsDel, 0);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User selectByStripeCustomerId(String stripeCustomerId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getStripeCustomerId, stripeCustomerId)
                .eq(User::getIsDel, 0);
        return userMapper.selectOne(queryWrapper);
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

    @Override
    public Integer updateIsSubscriptionByUserId(Integer userId, Integer isSubscription) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        queryWrapper.eq(User::getIsDel, 0);
        User user = userMapper.selectOne(queryWrapper);
        return userMapper.update(user.setIsSubscription(isSubscription), queryWrapper);
    }

    @Override
    public Long countIPByStartDateAndEndDate(String ip, LocalDateTime startDate, LocalDateTime endDate) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getIp, ip)
                .ge(User::getGmtCreate, startDate)
                .le(User::getGmtCreate, endDate)
                .eq(User::getIsDel, 0);
        return userMapper.selectCount(queryWrapper);
    }

}