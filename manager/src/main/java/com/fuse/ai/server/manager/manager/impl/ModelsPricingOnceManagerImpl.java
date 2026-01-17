package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.ModelsPricingOnce;
import com.fuse.ai.server.manager.manager.ModelsPricingOnceManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingOnceMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingOnceManagerImpl implements ModelsPricingOnceManager {

    @Resource
    private ModelsPricingOnceMapper modelsPricingOnceMapper;


    @Override
    public ModelsPricingOnce getDetailById(Integer id) {
        LambdaQueryWrapper<ModelsPricingOnce> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingOnce::getId, id);
        queryWrapper.eq(ModelsPricingOnce::getIsDel, 0);
        return modelsPricingOnceMapper.selectOne(queryWrapper);
    }

    public ModelsPricingOnce getDetailByModelId(Integer modelId) {
        LambdaQueryWrapper<ModelsPricingOnce> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingOnce::getModelId, modelId);
        queryWrapper.eq(ModelsPricingOnce::getIsDel, 0);
        return modelsPricingOnceMapper.selectOne(queryWrapper);
    }

}