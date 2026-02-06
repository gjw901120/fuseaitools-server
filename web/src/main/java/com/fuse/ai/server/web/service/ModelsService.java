package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.web.controller.ChatController.SseCallback;
import com.fuse.ai.server.web.model.vo.ModelPricingDetailVO;
import com.fuse.ai.server.web.model.vo.ModelsTreeVO;

import java.util.Map;

public interface ModelsService {

    ModelsTreeVO getModelsTree();

    Map<String, ModelPricingDetailVO> getModelsPrice();

    Models getModelByName(String modelName);

    Models getSseModelByName(String modelName, SseCallback callback);
}
