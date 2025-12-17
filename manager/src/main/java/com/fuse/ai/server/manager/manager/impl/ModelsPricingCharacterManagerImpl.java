package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.ModelsPricingCharacterManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingCharacterMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingCharacterManagerImpl implements ModelsPricingCharacterManager {

    @Resource
    private ModelsPricingCharacterMapper modelsPricingCharacterMapper;

}
