package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelConversion;

public interface UserModelConversionManager {

    Integer insert(UserModelConversion userModelConversion);

    UserModelConversion getDetailIdByUuId(String Uuid);

}
