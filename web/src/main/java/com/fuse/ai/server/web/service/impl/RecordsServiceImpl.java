package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.*;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.vo.RecordChatDetailVO;
import com.fuse.ai.server.web.model.vo.RecordDetailVO;
import com.fuse.ai.server.web.model.vo.RecordExtendVO;
import com.fuse.ai.server.web.model.vo.RecordVO;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.common.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ModelsCategoryManager modelsCategoryManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(Models model, String title, Object originalData, UserModelTask userModelTask,  verifyCreditsBO verifyCreditsBO) {

        String extractTitle = title.length() > 20 ? title.substring(0, 20).concat("...") : title;

        UserModelRecords userModelRecords = UserModelRecords.create(userModelTask.getUserId(), model.getId(), extractTitle, originalData, 0);

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

            String extractTitle = title.length() > 20 ? title.substring(0, 20).concat("...") : title;

            //根据模型名称获取id
            UserModelRecords userModelRecords = UserModelRecords.create(userModelConversation.getUserId(), modelId, extractTitle, new HashMap<>(), 0);

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
    public void completed(String taskId, List<String> outputUrl, Object outputResult, Object outputCallbackDetails) {

        UserModelTask userModelTask = userModelTaskManager.getDetailIdByTaskId(taskId);

        userModelTask.setOutputUrls(outputUrl);
        userModelTask.setOutputResult(outputResult);
        userModelTask.setOutputCallbackDetails(outputCallbackDetails);
        userModelTask.setStatus(TaskStatusEnum.SUCCESS.getCode());

        userModelTaskManager.updateById(userModelTask);


        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(userModelTask.getRecordId());
        userModelRecords.setIsCompleted(1);
        userModelRecords.setGmtCompleted(LocalDateTime.now());
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
                .multiply(modelsPricingToken.getPromptCredits())
                .divide(BigDecimal.valueOf(modelsPricingToken.getPromptTokens()),4, RoundingMode.HALF_UP);

        BigDecimal completionCredits = BigDecimal.valueOf(completionTokens)
                .multiply(modelsPricingToken.getCompletionCredits())
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
        userModelRecords.setGmtCompleted(LocalDateTime.now());
        userModelRecordsManager.updateById(userModelRecords);

        //去除冻结金额，更新状态
        Bill bill = billManager.getDetailByRecordId(userModelTask.getRecordId());
        bill.setStatus(BillStatusEnum.FAILED);
        billManager.updateById(bill);

        if(bill.getRechargeDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userRechargeCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.RECHARGE.getCode());
            BigDecimal blockCredits = userRechargeCredits.getBlockCredits().compareTo(bill.getRechargeDeductCredits()) > 0 ?
                    userRechargeCredits.getBlockCredits().subtract(bill.getRechargeDeductCredits()) : BigDecimal.ZERO;
            userRechargeCredits.setCredits(userRechargeCredits.getCredits().add(userRechargeCredits.getBlockCredits()).subtract(blockCredits));
            userRechargeCredits.setBlockCredits(blockCredits);
            userCreditsManager.updateById(userRechargeCredits);
        }

        if(bill.getSubscriptionDeductCredits().compareTo(BigDecimal.ZERO) > 0) {
            UserCredits userSubscriptionCredits = userCreditsManager.getDetailByUserIdAndType(userModelTask.getUserId(), UserCreditTypeEnum.SUBSCRIPTION.getCode());
            BigDecimal blockCredits = userSubscriptionCredits.getBlockCredits().compareTo(bill.getSubscriptionDeductCredits()) > 0 ?
                    userSubscriptionCredits.getBlockCredits().subtract(bill.getSubscriptionDeductCredits()) : BigDecimal.ZERO;
            userSubscriptionCredits.setCredits(userSubscriptionCredits.getCredits().add(userSubscriptionCredits.getBlockCredits()).subtract(blockCredits));
            userSubscriptionCredits.setBlockCredits(blockCredits);
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
                subscriptionDeductCredits = subscriptionOriginCredits;
                rechargeDeductCredits = deductCredits.subtract(subscriptionDeductCredits);
            }
            //如果是进行中的任务，冻结金额
            if(BillStatusEnum.PROGRESS.equals(billStatus)) {
                userSubscriptionCredits.setBlockCredits(userSubscriptionCredits.getBlockCredits().add(subscriptionDeductCredits));
            }
            userSubscriptionCredits.setCredits(subscriptionOriginCredits.subtract(subscriptionDeductCredits));
            userCreditsManager.updateById(userSubscriptionCredits);
        }
        if(!isSubscriptionCreditsEnough && userRechargeCredits != null && userRechargeCredits.getCredits().compareTo(BigDecimal.ZERO) > 0) {
            rechargeOriginCredits = userRechargeCredits.getCredits();
            if(rechargeDeductCredits.compareTo(BigDecimal.ZERO) > 0) {
                rechargeDeductCredits = rechargeOriginCredits.compareTo(rechargeDeductCredits) >= 0 ? rechargeDeductCredits : rechargeOriginCredits;
            } else {
                rechargeDeductCredits = rechargeOriginCredits.compareTo(deductCredits) >= 0 ? deductCredits : rechargeOriginCredits;
            }
            //如果是进行中的任务，冻结金额
            if(BillStatusEnum.PROGRESS.equals(billStatus)) {
                userRechargeCredits.setBlockCredits(userRechargeCredits.getBlockCredits().add(rechargeDeductCredits));
            }
            userRechargeCredits.setCredits(rechargeOriginCredits.subtract(rechargeDeductCredits));
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

    @Override
    public List<RecordVO> getList(Integer page, Integer size, Integer userId) {
        User user = userManager.selectById(userId);
        List<RecordVO> recordVOList = new ArrayList<>();
        List<Models> modelsList = modelsManager.getAll();
        Map<Integer, String> modelMap = modelsList.stream()
                .collect(Collectors.toMap(Models::getId, Models::getName));
        page = (page == null || page <= 0) ? 1 : page;
        size = (size == null || size <= 0) ? 10 : size;
        List<UserModelRecords> userModelRecordsList = userModelRecordsManager.getListByUserId(page, size, userId);
        List<ModelsCategory> categoryList = modelsCategoryManager.getAll();
        Map<Integer, String> modelsIdToCategoryNameMap = modelsList.stream()
                .collect(Collectors.toMap(
                        Models::getId,
                        model -> {
                            // 查找对应的分类名称
                            return categoryList.stream()
                                    .filter(category -> category.getId().equals(model.getCategoryId()))
                                    .findFirst()
                                    .map(ModelsCategory::getName)
                                    .orElse("");
                        },
                        (oldValue, newValue) -> oldValue  // 处理重复键
                ));
        for (UserModelRecords userModelRecords : userModelRecordsList) {
            RecordVO recordVO = new RecordVO();
            recordVO.setRecordId(userModelRecords.getUuid());
            recordVO.setModelId(userModelRecords.getModelId());
            recordVO.setCategory(modelsIdToCategoryNameMap.get(userModelRecords.getModelId()));
            recordVO.setIsCompleted(userModelRecords.getIsCompleted());
            //处理veo3特殊逻辑
            String model = modelMap.get(userModelRecords.getModelId()) == null ? "" : modelMap.get(userModelRecords.getModelId());
            if(VeoModelEnum.VEO3.getCode().equals(model) || VeoModelEnum.VEO3_FAST.getCode().equals(model)) {
                String generationType = "";
                if (userModelRecords.getOriginalData() != null) {
                    Map<String, Object> dataMap = (Map<String, Object>) userModelRecords.getOriginalData();
                    generationType = (String) dataMap.get("generationType");
                }
                recordVO.setModel(generationType);
            } else {
                recordVO.setModel(model);
            }

            recordVO.setTitle(userModelRecords.getTitle());
            recordVO.setGtmCreated(userModelRecords.getGmtCreate().plusHours(user.getTimeZoneOffset()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            recordVOList.add(recordVO);
        }
        return recordVOList;
    }

    @Override
    public RecordDetailVO getDetail(String recordId, Integer userId) {
        RecordDetailVO recordDetailVO = new RecordDetailVO();
        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(recordId);
        if (userModelRecords == null) {
            throw new BaseException(ResponseErrorType.RECORD_IS_NOT_EXIST, "Record is not exist");
        }
        UserModelTask userModelTask = userModelTaskManager.getDetailByRecordId(userModelRecords.getUuid());
        Models model = modelsManager.getDetailById(userModelRecords.getModelId());
        Bill bill = billManager.getDetailByRecordId(userModelRecords.getUuid());
        recordDetailVO.setRecordId(userModelRecords.getUuid());
        recordDetailVO.setModelId(userModelRecords.getModelId());
        recordDetailVO.setModel(model.getName());
        recordDetailVO.setStatus(userModelTask.getStatus());
        recordDetailVO.setTitle(userModelRecords.getTitle());
        recordDetailVO.setCredits(bill.getSubscriptionDeductCredits().add(bill.getRechargeDeductCredits()));
        recordDetailVO.setOriginalData(userModelRecords.getOriginalData());
        recordDetailVO.setOutputUrls(userModelTask.getOutputUrls());
        recordDetailVO.setOutputResults((Map<String, Object>) userModelTask.getOutputResult());

        return recordDetailVO;
    }

    @Override
    public RecordChatDetailVO getChatDetail(String recordId, Integer userId) {
        RecordChatDetailVO recordChatDetailVO = new RecordChatDetailVO();
        UserModelRecords userModelRecords = userModelRecordsManager.getDetailIdByUuId(recordId);
        if (userModelRecords == null) {
            throw new BaseException(ResponseErrorType.RECORD_IS_NOT_EXIST, "Record is not exist");
        }
        UserModelConversation userModelConversation = userModelConversationManager.getDetailIdByRecordId(recordId);
        List<UserModelConversationMessage> userModelConversationMessageList = userModelConversationMessageManager.selectByConversationId(userModelConversation.getUuid());
        recordChatDetailVO.setRecordId(userModelRecords.getUuid());
        recordChatDetailVO.setModelId(userModelRecords.getModelId());
        recordChatDetailVO.setModel(modelsManager.getDetailById(userModelRecords.getModelId()).getName());
        recordChatDetailVO.setConversionId(userModelConversation.getUuid());
        List<RecordChatDetailVO.MessageItem> messageItemList = new ArrayList<>();
        for (UserModelConversationMessage userModelConversationMessage : userModelConversationMessageList) {
            RecordChatDetailVO.MessageItem messageItem = new RecordChatDetailVO.MessageItem();
            messageItem.setMessage(userModelConversationMessage.getMessage());
            messageItem.setRole(userModelConversationMessage.getRole().getDescription());
            messageItem.setFileUrls(userModelConversationMessage.getFiles());
            messageItemList.add(messageItem);
        }
        recordChatDetailVO.setMessageList(messageItemList);
        return recordChatDetailVO;
    }

    @Override
    public List<RecordExtendVO> getExtendList(String model, Integer userId) {

        List<UserModelTask> userModelTaskList;
        List<UserModelRecords> userModelRecordsList;

        //处理veo3特殊逻辑
        if("veo3".equals(model)) {
            List<Models> modelsList = modelsManager.getDetailsByNames(Arrays.asList(VeoModelEnum.VEO3.getCode(), VeoModelEnum.VEO3_FAST.getCode()));
            List<Integer> modelIds = modelsList.stream().map(Models::getId).toList();
            userModelTaskList = userModelTaskManager.getListByModelIdsAndUserId(modelIds, userId);

            userModelRecordsList = userModelRecordsManager.getListByModelIdsAndUserId(modelIds, userId);
        } else {
            Models models = modelsManager.getDetailByName(model);
            if(models == null) {
                throw new BaseException(ResponseErrorType.MODEL_IS_NOT_EXIST, "Model is not exist");
            }
            userModelTaskList = userModelTaskManager.getListByModelIdAndUserId(models.getId(), userId);

            userModelRecordsList = userModelRecordsManager.getListByModelIdAndUserId(models.getId(), userId);
        }
        List<RecordExtendVO> recordExtendVOList = new ArrayList<>();
        Map<String, String> recordIdToTitleMap = userModelRecordsList.stream()
                .collect(Collectors.toMap(UserModelRecords::getUuid, UserModelRecords::getTitle));

        for (UserModelTask userModelTask : userModelTaskList) {
            RecordExtendVO recordExtendVO = new RecordExtendVO();
            recordExtendVO.setTaskId(userModelTask.getThirdTaskId());
            recordExtendVO.setRecordId(userModelTask.getRecordId());
            recordExtendVO.setTitle(recordIdToTitleMap.get(userModelTask.getRecordId()));
            recordExtendVO.setOutputUrls(userModelTask.getOutputUrls());
            recordExtendVOList.add(recordExtendVO);
        }

        return recordExtendVOList;
    }

    @Override
    public Boolean isCompleted(String taskId) {
        if(taskId == null || taskId.isEmpty()) {
            return true; // No task ID provided, consider it completed
        }
        return !userModelTaskManager.isExistByThirdTaskId(taskId); // Task ID does not exist, consider it completed
    }


}
