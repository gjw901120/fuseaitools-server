package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelConversion;
import com.fuse.ai.server.manager.manager.UserModelConversionManager;
import com.fuse.ai.server.manager.mapper.UserModelConversionMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserModelConversionManagerImpl implements UserModelConversionManager {

    @Resource
    private UserModelConversionMapper userModelConversionMapper;

    @Override
    public Integer insert(UserModelConversion userModelConversion) {
        userModelConversionMapper.insert(userModelConversion);
        return userModelConversion.getId();
    }

    @Override
    public UserModelConversion getDetailIdByUuId(String uuid) {
        LambdaQueryWrapper<UserModelConversion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelConversion::getUuid, uuid)
                .eq(UserModelConversion::getIsDel, 0);
        return userModelConversionMapper.selectOne(queryWrapper);
    }

}