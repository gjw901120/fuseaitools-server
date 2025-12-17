package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.ModelsPricingOnceManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingOnceMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingOnceManagerImpl implements ModelsPricingOnceManager {

    @Resource
    private ModelsPricingOnceMapper modelsPricingOnceMapper;

}