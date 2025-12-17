package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.enums.UserCreditTypeEnum;
import com.fuse.ai.server.manager.enums.UserRoleEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private UserModelRecordsManager userModelRecordsManager;

    @Autowired
    private UserModelTaskManager userModelTaskManager;

    @Autowired
    private ModelsManager modelsManager;

    @Autowired
    private UserModelConversionManager userModelConversionManager;

    @Autowired
    private UserModelConversionMessageManager userModelConversionMessageManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private BillManager billManager;

    @Autowired
    private UserCreditsManager userCreditsManager;

    @Autowired
    private  SubscriptionConfigManager subscriptionConfigManager;

    @Override
    @Transactional
    public String create(String model, String title, UserModelTask userModelTask) {
        //根据模型名称获取id
        Integer modelId = modelsManager.getModelIdByName(model);

        UserModelRecords userModelRecords = UserModelRecords.create(userModelTask.getUserId(), modelId, title,0);

        //写入记录
        userModelRecordsManager.insert(userModelRecords);

        userModelTask.setRecordId(userModelRecords.getUuid());
        userModelTask.setModelId(modelId);

        //写入任务
        userModelTaskManager.insert(userModelTask);

        return userModelRecords.getUuid();
    }

    @Override
    @Transactional
    public Integer create(String model, String title, UserModelConversion userModelConversion, UserModelConversionMessage userModelConversionMessage) {
        Integer modelId = modelsManager.getModelIdByName(model);
        //判断是第一次会话，还是续会话
        if("".equals(userModelConversionMessage.getConversionId())) {
            //根据模型名称获取id
            UserModelRecords userModelRecords = UserModelRecords.create(userModelConversion.getUserId(), modelId, title, 0);

            //写入记录
            userModelRecordsManager.insert(userModelRecords);

            userModelConversion.setRecordId(userModelRecords.getUuid());
            userModelConversion.setModelId(modelId);

            //写入任务
            userModelConversionManager.insert(userModelConversion);
            userModelConversionMessage.setConversionId(userModelConversion.getUuid());
            userModelConversionMessage.setModelId(modelId);
            userModelConversionMessageManager.insert(userModelConversionMessage);
            return userModelConversionMessage.getId();
        } else {
            userModelConversionMessage.setModelId(modelId);
            userModelConversionMessageManager.insert(userModelConversionMessage);
            return userModelConversionMessage.getId();
        }
    }

    @Override
    @Transactional
    public void completed(String taskId, List<String> outputUrl, Object outputCallbackDetails) {

        UserModelTask userModelTask = userModelTaskManager.getDetailIdByTaskId(taskId);

        userModelTask.setOutputUrls(outputUrl);
        userModelTask.setOutputCallbackDetails(outputCallbackDetails);
        userModelTask.setStatus(TaskStatusEnum.SUCCESS.getCode());

        userModelTaskManager.updateById(userModelTask);


        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelTask.getRecordId());
        userModelRecords.setIsCompleted(1);
        userModelRecords.setGmtCompleted(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        userModelRecordsManager.updateById(userModelRecords);

    }

    @Override
    @Transactional
    public void completed(Integer messageId, String contents, Integer promptTokens, Integer completionTokens) {
        UserModelConversionMessage oldUserModelConversionMessage = userModelConversionMessageManager.selectById(messageId);
        UserModelConversion userModelConversion = userModelConversionManager.getDetailIdByUuId(oldUserModelConversionMessage.getConversionId());
        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelConversion.getRecordId());
        UserModelConversionMessage newUserModelConversionMessage = UserModelConversionMessage.create(
                oldUserModelConversionMessage.getUserId(),
                oldUserModelConversionMessage.getModelId(),
                oldUserModelConversionMessage.getConversionId(),
                UserRoleEnum.ASSISTANT,
                contents,
                new ArrayList<>(),
                promptTokens,
                completionTokens
        );
        userModelConversionMessageManager.insert(newUserModelConversionMessage);
        userModelRecords.setIsCompleted(1);
        userModelRecords.setGmtCompleted(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        userModelRecordsManager.updateById(userModelRecords);
        //扣除消耗积分
        User user = userManager.selectById(userModelRecords.getUserId());

        //获取用户折扣
        SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailByPackage(user.getSubscriptionPackage().getCode());

        BigDecimal actualDeductCredits = BigDecimal.valueOf(Math.addExact(promptTokens, promptTokens));
        BigDecimal deductCredits = actualDeductCredits.multiply(subscriptionConfig.getDiscount());

        UserCreditTypeEnum userCreditType = UserCreditTypeEnum.RECHARGE;
        //订阅余额
        BigDecimal subscriptionCredits = BigDecimal.ZERO;
        //订阅金额是否满足
        boolean isSubscriptionCreditsEnough = false;

        //订阅用户查询本月积分是否还有剩余
        if(user.getIsSubscription() == 1 ) {
            UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userModelRecords.getUserId(), UserCreditTypeEnum.SUBSCRIPTION.getCode());
            if(userCredits.getCredits().compareTo(BigDecimal.ZERO) > 0) {
                userCreditType = UserCreditTypeEnum.SUBSCRIPTION;
                if(userCredits.getCredits().compareTo(deductCredits) > 0) {
                    isSubscriptionCreditsEnough = true;
                }
                subscriptionCredits = userCredits.getCredits().subtract(deductCredits);
                userCredits.setCredits(subscriptionCredits.compareTo(BigDecimal.ZERO) > 0 ? subscriptionCredits : BigDecimal.ZERO);
                userCreditsManager.updateById(userCredits);
            }
        }
        if(!isSubscriptionCreditsEnough) {
            UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userModelRecords.getUserId(), UserCreditTypeEnum.RECHARGE.getCode());
            userCredits.setCredits(userCredits.getCredits().compareTo(deductCredits) > 0 ? userCredits.getCredits().subtract(deductCredits) : BigDecimal.ZERO);
            userCreditsManager.updateById(userCredits);
        }

        billManager.insert(
                Bill.create(
                        user.getId(),
                        userModelRecords.getUuid(),
                        userModelRecords.getModelId(),
                        0 ,
                        newUserModelConversionMessage.getId(),
                        userCreditType,
                        actualDeductCredits,
                        deductCredits,
                        subscriptionCredits,
                        subscriptionConfig.getDiscount(),
                        0
                )
        );

    }

    @Override
    @Transactional
    public Boolean failed(String taskId, Object outputCallbackDetails) {

        UserModelTask userModelTask = userModelTaskManager.getDetailIdByTaskId(taskId);

        userModelTask.setOutputCallbackDetails(outputCallbackDetails);
        userModelTask.setStatus(TaskStatusEnum.FAILED.getCode());

        userModelTaskManager.updateById(userModelTask);


        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelTask.getRecordId());
        userModelRecords.setIsCompleted(1);
        userModelRecords.setGmtCompleted(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        userModelRecordsManager.updateById(userModelRecords);

        return true;
    }


}
