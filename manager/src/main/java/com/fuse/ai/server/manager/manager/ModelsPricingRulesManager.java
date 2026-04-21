package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingRules;

import java.util.List;

public interface ModelsPricingRulesManager {

    ModelsPricingRules getDetailByModelIdAndDurationQuality(Integer modelId, Integer duration, String quality);

    ModelsPricingRules getDetailByModelIdAndDurationSize(Integer modelId, Integer duration, String  size);

    ModelsPricingRules getDetailByModelIdAndDurationSizeScene(Integer modelId, Integer duration, String  size, String scene);

    ModelsPricingRules getDetailByModelIdAndDurationQualityScene(Integer modelId, Integer duration, String quality, String scene);

    ModelsPricingRules getDetailByModelIdAndDurationScene(Integer modelId, Integer duration, String scene);

    ModelsPricingRules getDetailByModelIdAndDuration(Integer modelId, Integer duration);

    ModelsPricingRules getDetailByModelIdAndQuality(Integer modelId, String quality);

    ModelsPricingRules getDetailByModelIdAndSize(Integer modelId, String size);

    ModelsPricingRules getDetailByModelIdAndSpeed(Integer modelId, String speed);

    ModelsPricingRules getDetailByModelIdAndBatchSize(Integer modelId, Integer batchSize);

    List<ModelsPricingRules> getAll();

}
