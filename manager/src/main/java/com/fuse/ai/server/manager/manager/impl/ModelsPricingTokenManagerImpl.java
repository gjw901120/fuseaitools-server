package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.ModelsPricingToken;
import com.fuse.ai.server.manager.manager.ModelsPricingTokenManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingTokenMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingTokenManagerImpl implements ModelsPricingTokenManager {

    @Resource
    private ModelsPricingTokenMapper modelsPricingTokenMapper;

    @Override
    public ModelsPricingToken getDetailByModelId(Integer modelId) {
        LambdaQueryWrapper<ModelsPricingToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingToken::getModelId, modelId)
                .eq(ModelsPricingToken::getIsDel, 0);
        return modelsPricingTokenMapper.selectOne(queryWrapper);
    }

}