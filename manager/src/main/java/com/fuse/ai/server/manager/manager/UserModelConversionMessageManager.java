package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelConversionMessage;

import java.util.List;

public interface UserModelConversionMessageManager {

    Integer insert(UserModelConversionMessage userModelConversionMessage);

    List<UserModelConversionMessage> selectByConversionId(String conversionId);

    UserModelConversionMessage selectById(Integer id);
}
