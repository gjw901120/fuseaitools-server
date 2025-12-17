package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.manager.UserModelTaskManager;
import com.fuse.ai.server.manager.mapper.UserModelTaskMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class UserModelTaskManagerImpl implements UserModelTaskManager {

    @Resource
    private UserModelTaskMapper userModelTaskMapper;

    @Override
    public Integer insert(UserModelTask userModelTask) {
        userModelTaskMapper.insert(userModelTask);
        return userModelTask.getId();
    }

    @Override
    public UserModelTask getDetailIdByTaskId(String thirdTaskId) {
        LambdaQueryWrapper<UserModelTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(UserModelTask::getThirdTaskId, thirdTaskId)
            .eq(UserModelTask::getIsDel, 0);
        return userModelTaskMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(UserModelTask userModelTask) {
        return userModelTaskMapper.updateById(userModelTask);
    }


}