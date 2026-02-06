package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.ModelsPricingOnce;

import java.util.List;

public interface ModelsPricingOnceManager {

    ModelsPricingOnce getDetailById(Integer id);

    ModelsPricingOnce getDetailByModelId(Integer modelId);

    List<ModelsPricingOnce> getAll();


}
