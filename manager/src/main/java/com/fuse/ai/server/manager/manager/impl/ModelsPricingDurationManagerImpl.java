package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.ModelsPricingDurationManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingDurationMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingDurationManagerImpl implements ModelsPricingDurationManager {

    @Resource
    private ModelsPricingDurationMapper modelsPricingDurationMapper;

}
