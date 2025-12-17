package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.ModelsPricingTokenManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingTokenMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingTokenManagerImpl implements ModelsPricingTokenManager {

    @Resource
    private ModelsPricingTokenMapper modelsPricingTokenMapper;

}