package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelConversation;
import com.fuse.ai.server.manager.manager.UserModelConversationManager;
import com.fuse.ai.server.manager.mapper.UserModelConversationMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserModelConversationManagerImpl implements UserModelConversationManager {

    @Resource
    private UserModelConversationMapper userModelConversationMapper;

    @Override
    public Integer insert(UserModelConversation userModelConversation) {
        userModelConversationMapper.insert(userModelConversation);
        return userModelConversation.getId();
    }

    @Override
    public UserModelConversation getDetailIdByUuId(String uuid) {
        LambdaQueryWrapper<UserModelConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelConversation::getUuid, uuid)
                .eq(UserModelConversation::getIsDel, 0);
        return userModelConversationMapper.selectOne(queryWrapper);
    }

}