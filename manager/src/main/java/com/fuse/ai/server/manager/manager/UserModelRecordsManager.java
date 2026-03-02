package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserModelRecords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserModelRecordsManager {

    String insert(UserModelRecords userModelRecords);

    UserModelRecords getDetailIdByUuId(String Uuid);

    List<UserModelRecords> getListByUserId(Integer page, Integer size,Integer userId);
    List<UserModelRecords> getListByUserId(Integer userId);

    Integer updateById(UserModelRecords userModelRecords);

    List<UserModelRecords> getListByModelIdAndUserId(@Param("modelId") Integer modelId, @Param("userId") Integer userId);
    List<UserModelRecords> getListByModelIdsAndUserId(@Param("modelIds") List<Integer> modelIds, @Param("userId") Integer userId);


}
