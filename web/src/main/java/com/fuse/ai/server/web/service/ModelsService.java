package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.web.controller.ChatController.SseCallback;
import com.fuse.ai.server.web.model.vo.ModelsTreeVO;

public interface ModelsService {

    ModelsTreeVO getModelsTree();

    Models getModelByName(String modelName);

    Models getSseModelByName(String modelName, SseCallback callback);
}
