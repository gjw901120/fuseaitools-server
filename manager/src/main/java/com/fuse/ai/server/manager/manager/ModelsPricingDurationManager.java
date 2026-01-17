package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingDuration;

public interface ModelsPricingDurationManager {

    ModelsPricingDuration getDetailByModelId(Integer modelId);

}