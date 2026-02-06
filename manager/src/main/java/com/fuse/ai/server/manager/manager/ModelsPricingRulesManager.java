package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingRules;

import java.util.List;

public interface ModelsPricingRulesManager {

    ModelsPricingRules getDetailByModelIdAndDurationQuality(Integer modelId, Integer duration, String  quality);

    ModelsPricingRules getDetailByModelIdAndDurationSize(Integer modelId, Integer duration, String  size);

    ModelsPricingRules getDetailByModelIdAndDuration(Integer modelId, Integer duration);

    ModelsPricingRules getDetailByModelIdAndQuality(Integer modelId, String quality);

    ModelsPricingRules getDetailByModelIdAndSpeed(Integer modelId, String speed);

    List<ModelsPricingRules> getAll();

}
