package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.manager.ModelsManager;
import com.fuse.ai.server.manager.mapper.ModelsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelsManagerImpl implements ModelsManager {

    @Resource
    private ModelsMapper modelsMapper;

    /**
     * 根据模型名称查询模型ID - 使用QueryWrapper
     */
    @Override
    public Integer getModelIdByName(String modelName) {
        if (modelName == null || modelName.trim().isEmpty()) {
            return null;
        }

        LambdaQueryWrapper<Models> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Models::getId)
                .eq(Models::getName, modelName.trim())
                .eq(Models::getIsDel, 0);

        Models model = modelsMapper.selectOne(queryWrapper);
        return model != null ? model.getId() : null;
    }

    @Override
    public List<Models> getAll() {
        return modelsMapper.selectList(new LambdaQueryWrapper<Models>()
                .eq(Models::getIsDel, 0));
    }

}