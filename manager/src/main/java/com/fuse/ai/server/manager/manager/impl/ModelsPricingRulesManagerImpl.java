package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.ModelsPricingRulesManager;
import com.fuse.ai.server.manager.mapper.ModelsPricingRulesMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ModelsPricingRulesManagerImpl implements ModelsPricingRulesManager {

    @Resource
    private ModelsPricingRulesMapper modelsPricingRulesMapper;

}