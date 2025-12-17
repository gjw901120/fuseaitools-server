package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.UserCredits;

public interface UserCreditsManager {

    Integer insert(UserCredits userCredits);

    UserCredits getDetailByUserIdAndType(Integer userId, Integer type);

    Integer updateById(UserCredits userCredits);

}