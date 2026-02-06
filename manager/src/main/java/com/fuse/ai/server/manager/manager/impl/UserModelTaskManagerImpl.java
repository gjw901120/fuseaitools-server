package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.manager.UserModelTaskManager;
import com.fuse.ai.server.manager.mapper.UserModelTaskMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


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
    public UserModelTask getDetailByRecordId(String recordId) {
        LambdaQueryWrapper<UserModelTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(UserModelTask::getRecordId, recordId)
            .eq(UserModelTask::getIsDel, 0);
        return userModelTaskMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(UserModelTask userModelTask) {
        return userModelTaskMapper.updateById(userModelTask);
    }

    @Override
    public List<UserModelTask> getListByModelIdAndUserId(Integer modelId, Integer userId) {
        LambdaQueryWrapper<UserModelTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(UserModelTask::getModelId, modelId)
            .eq(UserModelTask::getUserId, userId)
            .eq(UserModelTask::getStatus, TaskStatusEnum.SUCCESS.getCode())
            .eq(UserModelTask::getIsDel, 0);
        return userModelTaskMapper.selectList(queryWrapper);
    }

    @Override
    public List<UserModelTask> getListByModelIdsAndUserId(List<Integer> modelIds, Integer userId) {
        LambdaQueryWrapper<UserModelTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .in(UserModelTask::getModelId, modelIds)
            .eq(UserModelTask::getUserId, userId)
            .eq(UserModelTask::getStatus, TaskStatusEnum.SUCCESS.getCode())
            .eq(UserModelTask::getIsDel, 0);
        return userModelTaskMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean isExistByThirdTaskId(String thirdTaskId) {
        LambdaQueryWrapper<UserModelTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(UserModelTask::getThirdTaskId, thirdTaskId)
            .eq(UserModelTask::getStatus, TaskStatusEnum.PROCESSING.getCode());
        return userModelTaskMapper.selectOne(queryWrapper) != null;
    }




}