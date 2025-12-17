package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelTask;
import org.apache.ibatis.annotations.Param;

public interface UserModelTaskManager {

    Integer insert(UserModelTask userModelTask);

    UserModelTask getDetailIdByTaskId(@Param("thirdTaskId") String thirdTaskId);

    Integer updateById(UserModelTask userModelTask);

}
