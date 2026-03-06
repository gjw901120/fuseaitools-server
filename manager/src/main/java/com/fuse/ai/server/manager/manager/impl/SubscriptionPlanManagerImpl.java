package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuse.ai.server.manager.entity.SubscriptionPlan;
import com.fuse.ai.server.manager.manager.SubscriptionPlanManager;
import com.fuse.ai.server.manager.mapper.SubscriptionPlanMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionPlanManagerImpl extends ServiceImpl<SubscriptionPlanMapper, SubscriptionPlan>
        implements SubscriptionPlanManager {

    @Override
    public Integer insert(SubscriptionPlan subscriptionPlan) {
        this.save(subscriptionPlan);
        return subscriptionPlan.getId();
    }

    @Override
    public List<SubscriptionPlan> selectBySubscriptionId(Integer subscriptionId) {
        LambdaQueryWrapper<SubscriptionPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubscriptionPlan::getSubscriptionId, subscriptionId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void insertBatch(List<SubscriptionPlan> list) {
        // 调用父类的 saveBatch 方法
        this.saveBatch(list);
    }

    @Override
    public void updateStatusByIds(List<Integer> subscriptionIds, Integer status) {
        LambdaUpdateWrapper<SubscriptionPlan> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SubscriptionPlan::getId, subscriptionIds);
        updateWrapper.set(SubscriptionPlan::getStatus, status);
        this.update(updateWrapper);
    }

    @Override
    public List<SubscriptionPlan> selectByEndDate(LocalDate endDate) {
        LambdaQueryWrapper<SubscriptionPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubscriptionPlan::getEndDate, endDate);
        queryWrapper.eq(SubscriptionPlan::getStatus, 1);
        queryWrapper.eq(SubscriptionPlan::getIsDel, 0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SubscriptionPlan> selectByStartDate(LocalDate startDate) {
        LambdaQueryWrapper<SubscriptionPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubscriptionPlan::getStartDate, startDate);
        queryWrapper.eq(SubscriptionPlan::getStatus, 1);
        queryWrapper.eq(SubscriptionPlan::getIsDel, 0);
        return this.baseMapper.selectList(queryWrapper);
    }
}