package com.fuse.ai.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.ModelsCategory;
import com.fuse.ai.server.manager.entity.ModelsPricingOnce;
import com.fuse.ai.server.manager.entity.ModelsPricingRules;
import com.fuse.ai.server.manager.manager.ModelsCategoryManager;
import com.fuse.ai.server.manager.manager.ModelsManager;
import com.fuse.ai.server.manager.manager.ModelsPricingOnceManager;
import com.fuse.ai.server.manager.manager.ModelsPricingRulesManager;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.controller.ChatController.SseCallback;
import com.fuse.ai.server.web.exception.SseBaseException;
import com.fuse.ai.server.web.model.vo.ModelPricingDetailVO;
import com.fuse.ai.server.web.model.vo.ModelsTreeVO;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private ModelsPricingRulesManager modelsPricingRulesManager;

    @Autowired
    private ModelsPricingOnceManager modelsPricingOnceManager;

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


    public Map<String, ModelPricingDetailVO> getModelsPrice() {
        List<Models> models = modelsManager.getAll();
        List<ModelsPricingRules> rules = modelsPricingRulesManager.getAll();
        List<ModelsPricingOnce> onceList = modelsPricingOnceManager.getAll();

        Map<String, ModelPricingDetailVO> result = new HashMap<>();

        // 构建映射关系
        // 1. 一个modelId对应多个once记录
        Map<Integer, List<ModelsPricingOnce>> onceMapByModelId = onceList.stream()
                .filter(once -> once.getIsDel() == 0)
                .collect(Collectors.groupingBy(ModelsPricingOnce::getModelId));

        // 2. 一个pricingId（once.id）对应一个rule记录
        Map<Integer, ModelsPricingRules> ruleMapByPricingId = rules.stream()
                .filter(rule -> rule.getIsDel() == 0)
                .collect(Collectors.toMap(
                        ModelsPricingRules::getPricingId,
                        rule -> rule,
                        (existing, replacement) -> existing));

        // 遍历所有模型
        for (Models model : models) {

            String modelName = model.getName();
            Integer modelId = model.getId();
            Integer isPricingRules = model.getIsPricingRules();

            // 获取该模型的所有once记录
            List<ModelsPricingOnce> modelOnceList = onceMapByModelId.get(modelId);
            if (CollectionUtils.isEmpty(modelOnceList)) {
                continue;
            }

            // 创建定价信息对象
            ModelPricingDetailVO pricingVO = new ModelPricingDetailVO();

            if (isPricingRules == 0) {
                // 场景1：一次性定价
                pricingVO.setType("ONCE");

                List<ModelPricingDetailVO.OncePricing> oncePricingList = modelOnceList.stream()
                        .map(once -> new ModelPricingDetailVO.OncePricing(once.getCredits())).toList();

                pricingVO.setOnce(oncePricingList.get(0));

            } else if (isPricingRules == 1) {
                // 场景2：规则定价
                List<ModelPricingDetailVO.RulePricing> rulePricingList = new ArrayList<>();

                // 遍历该模型的所有once记录
                for (ModelsPricingOnce once : modelOnceList) {
                    Integer pricingId = once.getId();
                    BigDecimal credits = once.getCredits();

                    // 查找对应的rule记录
                    ModelsPricingRules rule = ruleMapByPricingId.get(pricingId);
                    if (rule != null) {
                        ModelPricingDetailVO.RulePricing rulePricing = new ModelPricingDetailVO.RulePricing(
                                credits,
                                rule.getDuration(),
                                rule.getQuality(),
                                rule.getSize(),
                                rule.getBatchSize(),
                                rule.getSpeed(),
                                rule.getScene()
                        );
                        rulePricingList.add(rulePricing);
                    } else {
                        log.warn("定价ID:{} 没有对应的规则信息，模型:{}", pricingId, modelName);
                    }
                }

                if (CollectionUtils.isEmpty(rulePricingList)) {
                    // 如果没有找到规则，降级为一次性定价
                    log.warn("模型 {} (ID:{}) 启用规则定价，但未找到规则，降级为一次性定价",
                            modelName, modelId);
                    pricingVO.setType("ONCE");
                    List<ModelPricingDetailVO.OncePricing> oncePricingList = modelOnceList.stream()
                            .map(once -> new ModelPricingDetailVO.OncePricing(once.getCredits())).toList();
                    pricingVO.setOnce(oncePricingList.get(0));
                } else {
                    pricingVO.setType("RULE");
                    pricingVO.setRules(rulePricingList);
                }
            } else {
                log.error("模型 {} (ID:{}) 的 isPricingRules 字段值非法: {}",
                        modelName, modelId, isPricingRules);
                continue;
            }

            result.put(modelName, pricingVO);
        }

        return result;

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
