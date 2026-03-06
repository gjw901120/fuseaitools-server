package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.SubscriptionPlan;
import com.fuse.ai.server.web.model.vo.OrderRefundVO;
import com.stripe.model.Event;

import java.util.List;

public interface OrderService {

    void createRechargeOrder(String customer, String paymentIntentId, String chargeId, Event event);

    void createSubscriptionOrder(String customer, String stripeOrderId, String paymentIntentId, String chargeId, Event event);

    void changeSubscriptionOrder(String customer, String stripeOrderId, String subscriptionId, Event event);


    void paymentIntentChange(String uuid, String priceId, String stripeOrderId, String paymentIntentId, Event event);

    void invoiceChange(String uuid, String priceId, String subscriptionId, String stripeOrderId, String invoiceBillingReason, Event event);

    void subscriptionChange(String userId, String status, boolean cancelAtPeriodEnd, Event event);

    void refundChange(String refundId, Event event);

    OrderRefundVO refundRecharge(Integer userId, Boolean isConfirm);

    OrderRefundVO refundSubscription(Integer userId, Boolean isConfirm);

    void cancelSubscription(Integer userId);

    List<SubscriptionPlan> refreshPlan();

}
