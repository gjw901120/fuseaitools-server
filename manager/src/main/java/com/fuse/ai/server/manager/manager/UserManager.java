package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.User;

public interface UserManager {

    Integer insert(User user);

    User selectById(Integer id);

    User selectByEmail(String email);

    User selectByThirdPartyId(String thirdPartyId);

    Integer updateById(User user);

}
