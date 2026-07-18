package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.User;

import java.time.LocalDateTime;

public interface UserManager {

    Integer insert(User user);

    User selectById(Integer id);

    User selectByUuid(String uuid);

    User selectByStripeCustomerId(String stripeCustomerId);

    User selectByEmail(String email);

    User selectByThirdPartyId(String thirdPartyId);

    Integer updateById(User user);

    Integer updateIsSubscriptionByUserId(Integer userId, Integer isSubscription);

    Long countIPByStartDateAndEndDate(String ip, LocalDateTime startDate, LocalDateTime endDate);

}
