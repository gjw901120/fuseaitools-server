package com.fuse.ai.server.manager.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuse.ai.server.manager.entity.SubscriptionPlan;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionPlanManager extends IService<SubscriptionPlan> {

    Integer insert(SubscriptionPlan subscriptionPlan);

    List<SubscriptionPlan> selectBySubscriptionId(Integer subscriptionId);

    void insertBatch(List<SubscriptionPlan> list);

    void updateStatusByIds(List<Integer> subscriptionIds, Integer status);

    List<SubscriptionPlan> selectByEndDate(LocalDate endDate);

    List<SubscriptionPlan> selectByStartDate(LocalDate startDate);

}
