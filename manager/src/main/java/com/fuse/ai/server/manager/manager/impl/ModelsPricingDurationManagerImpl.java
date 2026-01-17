package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.ModelsPricingDuration;
import com.fuse.ai.server.manager.manager.ModelsPricingDurationManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingDurationMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingDurationManagerImpl implements ModelsPricingDurationManager {

    @Resource
    private ModelsPricingDurationMapper modelsPricingDurationMapper;

    @Override
    public ModelsPricingDuration getDetailByModelId(Integer modelId) {
        LambdaQueryWrapper<ModelsPricingDuration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingDuration::getModelId, modelId);
        queryWrapper.eq(ModelsPricingDuration::getIsDel, 0);
        return modelsPricingDurationMapper.selectOne(queryWrapper);
    }

}
