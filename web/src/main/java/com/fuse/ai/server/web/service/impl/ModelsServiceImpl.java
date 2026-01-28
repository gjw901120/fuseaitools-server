package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.ModelsCategory;
import com.fuse.ai.server.manager.manager.ModelsCategoryManager;
import com.fuse.ai.server.manager.manager.ModelsManager;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.controller.ChatController.SseCallback;
import com.fuse.ai.server.web.exception.SseBaseException;
import com.fuse.ai.server.web.model.vo.ModelsTreeVO;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ModelsServiceImpl implements ModelsService {


    @Autowired
    private ModelsManager modelsManager;

    @Autowired
    private ModelsCategoryManager modelsCategoryManager;

    @Override
    public ModelsTreeVO getModelsTree() {
        ModelsTreeVO modelsTreeVO = new ModelsTreeVO();

        List<Models> models = modelsManager.getAll();

        List<ModelsCategory> modelsCategoryList = modelsCategoryManager.getAll();

        // 过滤已删除的数据（根据实际业务需求，可能需要过滤isDel=1的记录）
        List<Models> activeModels = models.stream()
                .filter(model -> model.getIsDel() == null || model.getIsDel() == 0).toList();

        List<ModelsCategory> activeCategories = modelsCategoryList.stream()
                .filter(category -> category.getIsDel() == null || category.getIsDel() == 0).toList();

        // 按categoryId分组模型
        Map<Integer, List<Models>> modelsByCategory = activeModels.stream()
                .collect(Collectors.groupingBy(Models::getCategoryId));

        // 构建分类详情列表
        List<ModelsTreeVO.CategoryDetailVO> categoryDetailVOList = activeCategories.stream()
                .map(category -> {
                    ModelsTreeVO.CategoryDetailVO categoryDetailVO = new ModelsTreeVO.CategoryDetailVO();
                    categoryDetailVO.setId(category.getId());
                    categoryDetailVO.setName(category.getName());

                    // 获取该分类下的模型列表
                    List<Models> categoryModels = modelsByCategory.getOrDefault(category.getId(), new ArrayList<>());

                    List<ModelsTreeVO.ModelDetailVO> modelDetailVOList = categoryModels.stream()
                            .map(model -> {
                                ModelsTreeVO.ModelDetailVO modelDetailVO = new ModelsTreeVO.ModelDetailVO();
                                modelDetailVO.setId(model.getId());
                                modelDetailVO.setName(model.getName());
                                modelDetailVO.setIsSearch(model.getIsSearch());
                                modelDetailVO.setIsThink(model.getIsThink());

                                // 处理ModelTypeEnum，可以根据需要转换为字符串
                                if (model.getType() != null) {
                                    modelDetailVO.setType(model.getType().name());
                                }

                                return modelDetailVO;
                            })
                            .collect(Collectors.toList());

                    categoryDetailVO.setModelList(modelDetailVOList);
                    return categoryDetailVO;
                })
                .collect(Collectors.toList());

        modelsTreeVO.setCategoryList(categoryDetailVOList);

        return modelsTreeVO;
    }

     @Override
    public Models getSseModelByName(String modelName, SseCallback callback) {
         Models model = modelsManager.getDetailByName(modelName);
         if (model == null) {
             SseBaseException.throwError(ResponseErrorType.MODEL_IS_NOT_EXIST, "model is not exist", callback);
         }
         return  model;
     }


    @Override
    public Models getModelByName(String modelName) {
        Models model = modelsManager.getDetailByName(modelName);
        if (model == null) {
            throw new BaseException(ResponseErrorType.MODEL_IS_NOT_EXIST, "model is not exist");
        }
        return  model;
    }

}
