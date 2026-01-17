package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelConversationMessage;

import java.util.List;

public interface UserModelConversationMessageManager {

    Integer insert(UserModelConversationMessage userModelConversationMessage);

    List<UserModelConversationMessage> selectByConversationId(String ConversationId);

    UserModelConversationMessage selectById(Integer id);
}
