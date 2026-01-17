package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.PricingTypeEnum;
import com.fuse.ai.server.manager.enums.UserCreditTypeEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class UserCreditsServiceImpl implements UserCreditsService {

    @Autowired
    private UserCreditsManager  userCreditsManager;

    @Autowired
    private ModelsPricingRulesManager modelsPricingRulesManager;

    @Autowired
    private ModelsPricingOnceManager modelsPricingOnceManager;

    @Autowired
    private ModelsPricingCharacterManager  modelsPricingCharacterManager;

    @Autowired
    private ModelsPricingDurationManager  modelsPricingDurationManager;

    @Autowired
    private SubscriptionConfigManager subscriptionConfigManager;

    @Autowired
    private UserManager  userManager;

    @Override
    public verifyCreditsBO verifyCredits(Integer userId, Models model, ExtraDataBO extraData) {
        /*
          1.判断用户余额是否充足
          2.token/character/duration类型 余额 >= 10 才可用
          3.once验证余额是否充足，包含折扣
         */
        BigDecimal credits = BigDecimal.valueOf(10);
        Integer  pricingRulesId = 0;
        if(PricingTypeEnum.CHARACTER.equals(model.getPricingType())) {
            ModelsPricingCharacter  pricingCharacter = modelsPricingCharacterManager.getDetailByModelId(model.getId());
            credits = BigDecimal.valueOf((extraData.getEleCharacter() / pricingCharacter.getCharacter()))
                    .multiply(pricingCharacter.getCreadits());
        } else if(PricingTypeEnum.DURATION.equals(model.getPricingType())) {
            ModelsPricingDuration  pricingDuration = modelsPricingDurationManager.getDetailByModelId(model.getId());
            credits  = BigDecimal.valueOf((extraData.getDuration() / pricingDuration.getDuration()))
                    .multiply(pricingDuration.getCreadits());
        } else if(PricingTypeEnum.ONCE.equals(model.getPricingType())){
            if(model.getIsPricingRules()  == 1) {
                ModelsPricingRules pricingRules = switch (extraData.getType()) {
                    case DURATION_QUALITY ->
                            modelsPricingRulesManager.getDetailByModelIdAndDurationQuality(model.getId(), extraData.getDuration(), extraData.getQuality());
                    case DURATION_SIZE ->
                            modelsPricingRulesManager.getDetailByModelIdAndDurationSize(model.getId(), extraData.getDuration(), extraData.getSize());
                    case DURATION ->
                            modelsPricingRulesManager.getDetailByModelIdAndDuration(model.getId(), extraData.getDuration());
                    case QUALITY ->
                            modelsPricingRulesManager.getDetailByModelIdAndQuality(model.getId(), extraData.getQuality());
                    default -> new ModelsPricingRules();
                };
                if(pricingRules == null) {
                    throw  new BaseException(ResponseErrorType.MODEL_IS_NOT_SUPPORT,  "model is not support");
                }
                pricingRulesId =  pricingRules.getId();
                ModelsPricingOnce modelsPricingOnce = modelsPricingOnceManager.getDetailById( pricingRules.getPricingId());
                credits =  modelsPricingOnce.getCreadits();
            } else {
                ModelsPricingOnce modelsPricingOnce = modelsPricingOnceManager.getDetailById( model.getId());
                credits =  modelsPricingOnce.getCreadits();
            }
        }

        //获取用户折扣
        User user = userManager.selectById(userId);

        SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailByPackage(user.getSubscriptionPackage().getCode());

        BigDecimal discount = subscriptionConfig == null ? BigDecimal.ONE :subscriptionConfig.getDiscount() ;

        BigDecimal discountCredits =  credits.multiply(discount).setScale( 4, RoundingMode.HALF_UP);

        //根据模型判断剩余金额校验逻辑
        UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType( userId, UserCreditTypeEnum.RECHARGE.getCode());
        if(userCredits  == null ||  userCredits.getCredits().compareTo(discountCredits) <= 0) {
            BigDecimal rechargeCredits =  userCredits == null ? BigDecimal.ZERO : userCredits.getCredits();
            UserCredits newUserCredits = userCreditsManager.getDetailByUserIdAndType( userId, UserCreditTypeEnum.SUBSCRIPTION.getCode());
             if(newUserCredits == null || newUserCredits.getCredits().add(rechargeCredits).compareTo(discountCredits) <= 0) {
                throw new BaseException(ResponseErrorType.CREDITS_IS_NOT_ENOUGH,"credits is not enough");
            }
        }

        verifyCreditsBO  verifyCreditsBO = new verifyCreditsBO();
        verifyCreditsBO.setShouldDeductCredits(credits);
        verifyCreditsBO.setPricingRulesId(pricingRulesId);
        verifyCreditsBO.setDiscount(discount);
        return verifyCreditsBO;
    }


}
