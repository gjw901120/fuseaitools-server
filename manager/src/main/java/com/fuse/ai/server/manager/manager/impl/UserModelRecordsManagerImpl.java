package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.UserModelRecords;
import com.fuse.ai.server.manager.manager.UserModelRecordsManager;
import com.fuse.ai.server.manager.mapper.UserModelRecordsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserModelRecordsManagerImpl implements UserModelRecordsManager {

    @Resource
    private UserModelRecordsMapper userModelRecordsMapper;

    @Override
    public String insert(UserModelRecords userModelRecords) {
        userModelRecordsMapper.insert(userModelRecords);
        return userModelRecords.getUuid();
    }


    @Override
    public UserModelRecords getDetailIdByUuId(String Uuid) {
        LambdaQueryWrapper<UserModelRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelRecords::getUuid, Uuid);
        return userModelRecordsMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserModelRecords> getListByUserId(Integer page, Integer size, Integer userId) {

        Page<UserModelRecords> pageInfo = new Page<>(page, size);

        LambdaQueryWrapper<UserModelRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelRecords::getUserId, userId)
                .orderByDesc(UserModelRecords::getGmtCreate);

        Page<UserModelRecords> resultPage = userModelRecordsMapper.selectPage(pageInfo, queryWrapper);

        return resultPage.getRecords();
    }

    @Override
    public List<UserModelRecords> getListByUserId(Integer userId) {

        LambdaQueryWrapper<UserModelRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelRecords::getUserId, userId)
                .orderByDesc(UserModelRecords::getGmtCreate);

        return userModelRecordsMapper.selectList(queryWrapper);
    }

    @Override
    public Integer updateById(UserModelRecords userModelRecords) {
        return userModelRecordsMapper.updateById(userModelRecords);
    }

    @Override
    public List<UserModelRecords> getListByModelIdAndUserId(Integer modelId, Integer userId) {
        LambdaQueryWrapper<UserModelRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelRecords::getUserId, userId)
                .eq(UserModelRecords::getModelId, modelId)
                .eq(UserModelRecords::getIsCompleted, 1);
        return userModelRecordsMapper.selectList(queryWrapper);
    }

    @Override
    public List<UserModelRecords> getListByModelIdsAndUserId(List<Integer> modelIds, Integer userId) {
        LambdaQueryWrapper<UserModelRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelRecords::getUserId, userId)
                .in(UserModelRecords::getModelId, modelIds)
                .eq(UserModelRecords::getIsCompleted, 1);
        return userModelRecordsMapper.selectList(queryWrapper);
    }

}