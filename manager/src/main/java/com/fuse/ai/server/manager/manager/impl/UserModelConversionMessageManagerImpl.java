package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelConversionMessage;
import com.fuse.ai.server.manager.manager.UserModelConversionMessageManager;
import com.fuse.ai.server.manager.mapper.UserModelConversionMessageMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserModelConversionMessageManagerImpl implements UserModelConversionMessageManager {

    @Resource
    private UserModelConversionMessageMapper userModelConversionMessageMapper;

    @Override
    public Integer insert(UserModelConversionMessage userModelConversionMessage) {
        userModelConversionMessageMapper.insert(userModelConversionMessage);
        return userModelConversionMessage.getId();
    }

    @Override
    public List<UserModelConversionMessage> selectByConversionId(String conversionId) {
        LambdaQueryWrapper<UserModelConversionMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelConversionMessage::getConversionId, conversionId)
                .eq(UserModelConversionMessage::getIsDel, 0)
                .orderByAsc(UserModelConversionMessage::getGmtCreate);
        return userModelConversionMessageMapper.selectList(queryWrapper);
    }

    @Override
    public UserModelConversionMessage selectById(Integer id) {
        return userModelConversionMessageMapper.selectById(id);
    }

}