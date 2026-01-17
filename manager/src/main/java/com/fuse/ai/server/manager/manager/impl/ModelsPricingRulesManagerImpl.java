package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.ModelsPricingRules;
import com.fuse.ai.server.manager.manager.ModelsPricingRulesManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingRulesMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ModelsPricingRulesManagerImpl implements ModelsPricingRulesManager {

    @Resource
    private ModelsPricingRulesMapper modelsPricingRulesMapper;

    @Override
    public ModelsPricingRules getDetailByModelIdAndDurationQuality(Integer modelId, Integer duration, String  quality) {
        LambdaQueryWrapper<ModelsPricingRules> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingRules::getModelId, modelId);
        queryWrapper.eq(ModelsPricingRules::getQuality, quality);
        queryWrapper.eq(ModelsPricingRules::getDuration, duration);
        queryWrapper.eq(ModelsPricingRules::getIsDel, 0);
        return modelsPricingRulesMapper.selectOne(queryWrapper);
    }

    @Override
    public ModelsPricingRules getDetailByModelIdAndDurationSize(Integer modelId, Integer duration, String  size) {
        LambdaQueryWrapper<ModelsPricingRules> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingRules::getModelId, modelId);
        queryWrapper.eq(ModelsPricingRules::getSize, size);
        queryWrapper.eq(ModelsPricingRules::getDuration, duration);
        queryWrapper.eq(ModelsPricingRules::getIsDel, 0);
        return modelsPricingRulesMapper.selectOne(queryWrapper);
    }

    @Override
    public ModelsPricingRules getDetailByModelIdAndDuration(Integer modelId, Integer duration) {
        LambdaQueryWrapper<ModelsPricingRules> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingRules::getModelId, modelId);
        queryWrapper.eq(ModelsPricingRules::getDuration, duration);
        queryWrapper.eq(ModelsPricingRules::getIsDel, 0);
        return modelsPricingRulesMapper.selectOne(queryWrapper);
    }

    @Override
    public ModelsPricingRules getDetailByModelIdAndQuality(Integer modelId, String quality) {
        LambdaQueryWrapper<ModelsPricingRules> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingRules::getModelId, modelId);
        queryWrapper.eq(ModelsPricingRules::getQuality, quality);
        queryWrapper.eq(ModelsPricingRules::getIsDel, 0);
        return modelsPricingRulesMapper.selectOne(queryWrapper);
    }

}