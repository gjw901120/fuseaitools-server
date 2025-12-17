package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.entity.ModelsCategory;
import com.fuse.ai.server.manager.manager.ModelsCategoryManager;
import com.fuse.ai.server.manager.mapper.ModelsCategoryMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelsCategoryManagerImpl implements ModelsCategoryManager {

    @Resource
    private ModelsCategoryMapper modelsCategoryMapper;

    @Override
    public List<ModelsCategory> getAll() {
        return modelsCategoryMapper.selectList(null);
    }

}
