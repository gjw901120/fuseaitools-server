package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingOnce;

public interface ModelsPricingOnceManager {

    ModelsPricingOnce getDetailById(Integer id);

    ModelsPricingOnce getDetailByModelId(Integer modelId);


}
