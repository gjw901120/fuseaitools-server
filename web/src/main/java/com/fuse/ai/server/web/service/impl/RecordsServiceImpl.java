package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.BillStatusEnum;
import com.fuse.ai.server.manager.enums.TaskStatusEnum;
import com.fuse.ai.server.manager.enums.UserCreditTypeEnum;
import com.fuse.ai.server.manager.enums.UserRoleEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private UserModelConversationManager userModelConversationManager;

    @Autowired
    private UserModelConversationMessageManager userModelConversationMessageManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private BillManager billManager;

    @Autowired
    private UserCreditsManager userCreditsManager;

    @Autowired
    private  SubscriptionConfigManager subscriptionConfigManager;

    @Autowired
    private ModelsPricingTokenManager modelsPricingTokenManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(Models model, String title, UserModelTask userModelTask,  verifyCreditsBO verifyCreditsBO) {

        String extractTitle = title.length() > 30 ? title.substring(0, 30).concat("...") : title;

        UserModelRecords userModelRecords = UserModelRecords.create(userModelTask.getUserId(), model.getId(), extractTitle,0);

        //写入记录
        userModelRecordsManager.insert(userModelRecords);

        userModelTask.setRecordId(userModelRecords.getUuid());
        userModelTask.setModelId(model.getId());

        //写入任务
        userModelTaskManager.insert(userModelTask);

        deductCreditsAndCreateBill(userModelTask.getUserId(), model.getId(), userModelRecords.getUuid(), 0,
                verifyCreditsBO.getPricingRulesId(), verifyCreditsBO.getShouldDeductCredits(), verifyCreditsBO.getDiscount(), BillStatusEnum.PROGRESS);

        return userModelRecords.getUuid();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer create(String model, String title, UserModelConversation userModelConversation, UserModelConversationMessage userModelConversationMessage) {
        Integer modelId = modelsManager.getModelIdByName(model);
        //判断是第一次会话，还是续会话
        if("".equals(userModelConversationMessage.getConversationId())) {
            //根据模型名称获取id
            UserModelRecords userModelRecords = UserModelRecords.create(userModelConversation.getUserId(), modelId, title, 0);

            //写入记录
            userModelRecordsManager.insert(userModelRecords);

            userModelConversation.setRecordId(userModelRecords.getUuid());
            userModelConversation.setModelId(modelId);

            //写入任务
            userModelConversationManager.insert(userModelConversation);
            userModelConversationMessage.setConversationId(userModelConversation.getUuid());
            userModelConversationMessage.setModelId(modelId);
            userModelConversationMessageManager.insert(userModelConversationMessage);
            return userModelConversationMessage.getId();
        } else {
            userModelConversationMessage.setModelId(modelId);
            userModelConversationMessageManager.insert(userModelConversationMessage);
            return userModelConversationMessage.getId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        //去除冻结金额，更新状态
        Bill bill = billManager.getDetailByRecordId(userModelTask.getRecordId());
        bill.setStatus(BillStatusEnum.COMPLETED);
        billManager.updateById(bill);

        if(bill.getRechargeDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userRechargeCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.RECHARGE.getCode());
            BigDecimal blockCredits = userRechargeCredits.getBlockCredits().compareTo(bill.getRechargeDeductCredits()) > 0 ?
                    userRechargeCredits.getBlockCredits().subtract(bill.getRechargeDeductCredits()) : BigDecimal.ZERO;
            userRechargeCredits.setBlockCredits(blockCredits);
            userCreditsManager.updateById(userRechargeCredits);
        }

        if(bill.getSubscriptionDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userSubscriptionCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.SUBSCRIPTION.getCode());
            BigDecimal blockCredits = userSubscriptionCredits.getBlockCredits().compareTo(bill.getRechargeDeductCredits()) > 0 ?
                    userSubscriptionCredits.getBlockCredits().subtract(bill.getRechargeDeductCredits()) : BigDecimal.ZERO;
            userSubscriptionCredits.setBlockCredits(blockCredits);
            userCreditsManager.updateById(userSubscriptionCredits);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String completed(Integer messageId, String contents, Integer promptTokens, Integer completionTokens) {
        UserModelConversationMessage oldUserModelConversationMessage = userModelConversationMessageManager.selectById(messageId);
        UserModelConversation userModelConversation = userModelConversationManager.getDetailIdByUuId(oldUserModelConversationMessage.getConversationId());
        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelConversation.getRecordId());
        UserModelConversationMessage newUserModelConversationMessage = UserModelConversationMessage.create(
                oldUserModelConversationMessage.getUserId(),
                oldUserModelConversationMessage.getModelId(),
                oldUserModelConversationMessage.getConversationId(),
                UserRoleEnum.ASSISTANT,
                contents,
                new ArrayList<>(),
                promptTokens,
                completionTokens
        );
        ModelsPricingToken modelsPricingToken = modelsPricingTokenManager.getDetailByModelId(userModelConversation.getModelId());
        userModelConversationMessageManager.insert(newUserModelConversationMessage);
        userModelRecords.setIsCompleted(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        userModelRecords.setGmtCompleted(LocalDateTime.parse(formattedTime, formatter));
        userModelRecordsManager.updateById(userModelRecords);
        //扣除消耗积分
        User user = userManager.selectById(userModelRecords.getUserId());

        //获取用户折扣
        SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailByPackage(user.getSubscriptionPackage().getCode());

        BigDecimal discount = subscriptionConfig == null ? BigDecimal.ONE :subscriptionConfig.getDiscount() ;

        BigDecimal promptCredits = BigDecimal.valueOf(promptTokens)
                .multiply(modelsPricingToken.getPromptCreadits())
                .divide(BigDecimal.valueOf(modelsPricingToken.getPromptTokens()),4, RoundingMode.HALF_UP);

        BigDecimal completionCredits = BigDecimal.valueOf(completionTokens)
                .multiply(modelsPricingToken.getCompletionCreadits())
                .divide(BigDecimal.valueOf(modelsPricingToken.getCompletionTokens()),4, RoundingMode.HALF_UP);

        //应扣除积分
        BigDecimal shouldDeductCredits = promptCredits.add(completionCredits);

        deductCreditsAndCreateBill(user.getId(), userModelConversation.getModelId(), userModelRecords.getUuid(), messageId, 0 ,
                shouldDeductCredits, discount, BillStatusEnum.COMPLETED);

        return userModelConversation.getUuid();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean failed(String taskId, Object outputCallbackDetails) {

        UserModelTask userModelTask = userModelTaskManager.getDetailIdByTaskId(taskId);

        userModelTask.setOutputCallbackDetails(outputCallbackDetails);
        userModelTask.setStatus(TaskStatusEnum.FAILED.getCode());

        userModelTaskManager.updateById(userModelTask);


        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelTask.getRecordId());
        userModelRecords.setIsCompleted(1);
        userModelRecords.setGmtCompleted(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        userModelRecordsManager.updateById(userModelRecords);

        //去除冻结金额，更新状态
        Bill bill = billManager.getDetailByRecordId(userModelTask.getRecordId());
        bill.setStatus(BillStatusEnum.FAILED);
        billManager.updateById(bill);

        if(bill.getRechargeDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userRechargeCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.RECHARGE.getCode());
            BigDecimal blockCredits = userRechargeCredits.getBlockCredits().compareTo(bill.getRechargeDeductCredits()) > 0 ?
                    userRechargeCredits.getBlockCredits().subtract(bill.getRechargeDeductCredits()) : BigDecimal.ZERO;
            userRechargeCredits.setBlockCredits(blockCredits);
            userRechargeCredits.setCredits(userRechargeCredits.getCredits().add(userRechargeCredits.getBlockCredits()).subtract(blockCredits));
            userCreditsManager.updateById(userRechargeCredits);
        }

        if(bill.getSubscriptionDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userSubscriptionCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.SUBSCRIPTION.getCode());
            BigDecimal blockCredits = userSubscriptionCredits.getBlockCredits().compareTo(bill.getRechargeDeductCredits()) > 0 ?
                    userSubscriptionCredits.getBlockCredits().subtract(bill.getRechargeDeductCredits()) : BigDecimal.ZERO;
            userSubscriptionCredits.setBlockCredits(blockCredits);
            userSubscriptionCredits.setCredits(userSubscriptionCredits.getCredits().add(userSubscriptionCredits.getBlockCredits()).subtract(blockCredits));
            userCreditsManager.updateById(userSubscriptionCredits);
        }

        return true;
    }

    private void deductCreditsAndCreateBill(Integer userId, Integer modelId, String recordId, Integer messageId, Integer pricingId,
                       BigDecimal shouldDeductCredits, BigDecimal discount, BillStatusEnum billStatus) {
        //实际扣除积分
        BigDecimal deductCredits = shouldDeductCredits.multiply(discount);

        UserCreditTypeEnum userCreditType = UserCreditTypeEnum.RECHARGE;
        //扣除订阅积分
        BigDecimal subscriptionDeductCredits = BigDecimal.ZERO;
        //扣除充值积分
        BigDecimal rechargeDeductCredits = BigDecimal.ZERO;
        //订阅原始积分
        BigDecimal subscriptionOriginCredits = BigDecimal.ZERO;
        //充值原始积分
        BigDecimal rechargeOriginCredits = BigDecimal.ZERO;

        UserCredits userSubscriptionCredits = userCreditsManager.getDetailByUserIdAndType(userId, UserCreditTypeEnum.SUBSCRIPTION.getCode());
        UserCredits userRechargeCredits = userCreditsManager.getDetailByUserIdAndType(userId, UserCreditTypeEnum.RECHARGE.getCode());

        //订阅金额是否满足
        boolean isSubscriptionCreditsEnough = false;

        //订阅用户查询本月积分是否还有剩余
        if(userSubscriptionCredits != null && userSubscriptionCredits.getCredits().compareTo(BigDecimal.ZERO) > 0 ) {
            subscriptionOriginCredits = userSubscriptionCredits.getCredits();
            if(userSubscriptionCredits.getCredits().compareTo(deductCredits) >= 0) {
                userCreditType = UserCreditTypeEnum.SUBSCRIPTION;
                subscriptionDeductCredits = deductCredits;
                isSubscriptionCreditsEnough = true;
            } else {
                userCreditType = UserCreditTypeEnum.RECHARGE_SUBSCRIPTION;
                subscriptionDeductCredits = userSubscriptionCredits.getCredits();
                rechargeDeductCredits = deductCredits.subtract(subscriptionDeductCredits);
            }
            //如果是进行中的任务，冻结金额
            if(BillStatusEnum.PROGRESS.equals(billStatus)) {
                userSubscriptionCredits.setBlockCredits(subscriptionDeductCredits);
            }
            userSubscriptionCredits.setCredits(subscriptionDeductCredits);
            userCreditsManager.updateById(userSubscriptionCredits);
        }
        if(!isSubscriptionCreditsEnough && userRechargeCredits != null && userRechargeCredits.getCredits().compareTo(BigDecimal.ZERO) > 0) {
            rechargeOriginCredits = userRechargeCredits.getCredits();
            if(rechargeDeductCredits.compareTo(BigDecimal.ZERO) > 0) {
                rechargeDeductCredits = userRechargeCredits.getCredits().compareTo(rechargeDeductCredits) >= 0 ? rechargeDeductCredits : userRechargeCredits.getCredits();
            } else {
                rechargeDeductCredits = userRechargeCredits.getCredits().compareTo(deductCredits) >= 0 ? deductCredits : userRechargeCredits.getCredits();
            }
            //如果是进行中的任务，冻结金额
            if(BillStatusEnum.PROGRESS.equals(billStatus)) {
                userRechargeCredits.setBlockCredits(rechargeDeductCredits);
            }
            userRechargeCredits.setCredits(rechargeDeductCredits);
            userCreditsManager.updateById(userRechargeCredits);
        }

        billManager.insert(
                Bill.create(
                        userId,
                        recordId,
                        modelId,
                        pricingId,
                        messageId,
                        userCreditType,
                        subscriptionDeductCredits,
                        rechargeDeductCredits,
                        shouldDeductCredits,
                        subscriptionOriginCredits,
                        rechargeOriginCredits,
                        discount,
                        billStatus,
                        0
                )
        );
    }


}
