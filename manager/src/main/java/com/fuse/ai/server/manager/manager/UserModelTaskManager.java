package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserModelTaskManager {

    Integer insert(UserModelTask userModelTask);

    UserModelTask getDetailIdByTaskId(@Param("thirdTaskId") String thirdTaskId);

    UserModelTask getDetailByRecordId(@Param("recordId") String recordId);

    Integer updateById(UserModelTask userModelTask);

    List<UserModelTask> getListByModelIdAndUserId(@Param("modelId") Integer modelId, @Param("userId") Integer userId);
    List<UserModelTask> getListByModelIdsAndUserId(@Param("modelIds") List<Integer> modelIds, @Param("userId") Integer userId);

    Boolean isExistByThirdTaskId(@Param("thirdTaskId") String thirdTaskId);

}
