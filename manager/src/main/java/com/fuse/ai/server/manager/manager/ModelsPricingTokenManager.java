package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingToken;

public interface ModelsPricingTokenManager {

    ModelsPricingToken getDetailByModelId(Integer modelId);

}
