package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.UserModelConversationMessage;
import com.fuse.ai.server.manager.manager.UserModelConversationMessageManager;
import com.fuse.ai.server.manager.mapper.UserModelConversationMessageMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserModelConversationMessageManagerImpl implements UserModelConversationMessageManager {

    @Resource
    private UserModelConversationMessageMapper userModelConversationMessageMapper;

    @Override
    public Integer insert(UserModelConversationMessage userModelConversationMessage) {
        userModelConversationMessageMapper.insert(userModelConversationMessage);
        return userModelConversationMessage.getId();
    }

    @Override
    public List<UserModelConversationMessage> selectByConversationId(String ConversationId) {
        LambdaQueryWrapper<UserModelConversationMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(UserModelConversationMessage::getConversationId, ConversationId)
                .eq(UserModelConversationMessage::getIsDel, 0)
                .orderByAsc(UserModelConversationMessage::getGmtCreate);
        return userModelConversationMessageMapper.selectList(queryWrapper);
    }

    @Override
    public UserModelConversationMessage selectById(Integer id) {
        return userModelConversationMessageMapper.selectById(id);
    }

}