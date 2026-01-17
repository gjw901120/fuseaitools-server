package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelConversation;

public interface UserModelConversationManager {

    Integer insert(UserModelConversation userModelConversation);

    UserModelConversation getDetailIdByUuId(String Uuid);

}
