package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.ModelsPricingCharacter;
import com.fuse.ai.server.manager.manager.ModelsPricingCharacterManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingCharacterMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingCharacterManagerImpl implements ModelsPricingCharacterManager {

    @Resource
    private ModelsPricingCharacterMapper modelsPricingCharacterMapper;

    @Override
    public ModelsPricingCharacter getDetailByModelId(Integer modelId) {
        LambdaQueryWrapper<ModelsPricingCharacter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ModelsPricingCharacter::getModelId, modelId);
        queryWrapper.eq(ModelsPricingCharacter::getIsDel, 0);
        return modelsPricingCharacterMapper.selectOne(queryWrapper);
    }

}
