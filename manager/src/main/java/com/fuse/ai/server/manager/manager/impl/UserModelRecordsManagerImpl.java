package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelRecords;
import com.fuse.ai.server.manager.manager.UserModelRecordsManager;
import com.fuse.ai.server.manager.mapper.UserModelRecordsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public Integer updateById(UserModelRecords userModelRecords) {
        return userModelRecordsMapper.updateById(userModelRecords);
    }

}