package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.OrderTypeEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.model.dto.request.stripe.CreateSessionDTO;
import com.fuse.ai.server.web.model.dto.response.CreateSessionResponse;
import com.fuse.ai.server.web.service.OrderService;
import com.fuse.ai.server.web.service.StripeService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import com.fuse.common.core.exception.error.UserErrorType;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceListParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.secret-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private UserManager userManager;

    @Autowired
    private SubscriptionConfigManager subscriptionConfigManager;

    @Autowired
    private RechargeConfigManager rechargeConfigManager;

    @Autowired
    private OrderService orderService;

    @Value("${webClient.url}")
    private String webClientUrl;

    @Autowired
    private OrderRefundManager orderRefundManager;

    @Autowired
    private OrderManager orderManager;

    @Override
    public CreateSessionResponse createSession(CreateSessionDTO createSessionDTO, Integer userId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // 判断用户是否已经拥有Stripe客户ID
        User user = userManager.selectById(userId);
        String stripeCustomerId = user.getStripeCustomerId();
        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            String email = (user.getEmail() != null && !user.getEmail().isEmpty()) ? user.getEmail() : user.getThirdPartyId().concat("@fuse.com");
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setEmail(email)
                    .putMetadata("app_user_id", user.getUuid())
                    .build();
            Customer customer = Customer.create(customerCreateParams);
            user.setStripeCustomerId(customer.getId());
            userManager.updateById(user);
            stripeCustomerId = customer.getId();
        }

        // 获取priceId
        String priceId;
        BigDecimal price;
        boolean isSubscription = "subscription".equals(createSessionDTO.getType());

        if (isSubscription) {
            if (user.getIsSubscription() == 1) {
                throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has already subscribed");
            }
            SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailById(createSessionDTO.getPriceId());
            priceId = subscriptionConfig.getStripePriceId();
            price = subscriptionConfig.getCost();
        } else {
            RechargeConfig rechargeConfig = rechargeConfigManager.getDetailById(createSessionDTO.getPriceId());
            priceId = rechargeConfig.getStripePriceId();
            price = rechargeConfig.getCost();
        }

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(isSubscription ? SessionCreateParams.Mode.SUBSCRIPTION : SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(webClientUrl + "/credits")
                .setCancelUrl(webClientUrl + "/pricing")
                .setCustomer(stripeCustomerId)
                .setClientReferenceId(user.getUuid())
                .putMetadata("app_user_id", user.getUuid())
                .putMetadata("price_id", priceId)
                .putMetadata("type", createSessionDTO.getType());

        if (isSubscription) {
            // 订阅模式：使用 Price ID
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPrice(priceId)
                            .setQuantity(1L)
                            .build()
            );

            // 订阅模式：设置 SubscriptionData
            paramsBuilder.setSubscriptionData(
                    SessionCreateParams.SubscriptionData.builder()
                            .putMetadata("app_user_id", user.getUuid())
                            .putMetadata("price_id", priceId)
                            .putMetadata("type", "subscription")
                            .build()
            );
        } else {
            // 支付模式：使用 PriceData 动态创建
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("FuseAI Credits")
                            .setDescription("AI credits for FuseAI platform")
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setProductData(productData)
                            .setCurrency("usd")
                            .setUnitAmount(price.multiply(BigDecimal.valueOf(100)).longValue())
                            .build();

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build()
            );

            // 支付模式：设置 PaymentIntentData
            paramsBuilder.setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                            .putMetadata("app_user_id", user.getUuid())
                            .putMetadata("price_id", priceId)
                            .putMetadata("type", "recharge")
                            .build()
            );
        }

        Session session = Session.create(paramsBuilder.build());

        CreateSessionResponse response = new CreateSessionResponse();
        response.setSessionId(session.getId());
        response.setSessionUrl(session.getUrl());
        return response;
    }

    @Override
    public String handleStripeWebhook(String payload, String signature) {
        try {
            Event event = Webhook.constructEvent(payload, signature, webhookSecret);
            log.info("Stripe webhook Event: type={}, id={}", event.getType(), event.getId());

            switch (event.getType()) {
                case "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event);
                case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
                case "invoice.paid" -> handleInvoicePaid(event);
                case "customer.subscription.updated" -> handleSubscriptionUpdated(event);
                case "charge.refund.updated" -> handleChargeRefundUpdated(event);
                case "charge.refunded" -> handleChargeRefunded(event);
                default -> log.info("Unhandled event type: {}", event.getType());
            }
            return "success";
        } catch (Exception e) {
            log.error("Stripe webhook处理失败", e);
            return "error";
        }
    }

    // ==================== PaymentIntent ====================
    private void handlePaymentIntentSucceeded(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("PaymentIntent反序列化失败");
            return;
        }

        PaymentIntent paymentIntent = (PaymentIntent) deserializer.getObject().get();
        //判断处理订阅or充值
        String type = paymentIntent.getMetadata().get("type");
        if("recharge".equals(type)) {
            orderService.createRechargeOrder(
                    paymentIntent.getCustomer(),
                    paymentIntent.getId(),
                    paymentIntent.getLatestCharge(),
                    event
            );
        } else {
            orderService.createSubscriptionOrder(
                    paymentIntent.getCustomer(),
                    paymentIntent.getPaymentDetails().getOrderReference(),
                    paymentIntent.getId(),
                    paymentIntent.getLatestCharge(),
                    event
            );
        }


    }

    // ==================== Checkout Session ====================
    private void handleCheckoutSessionCompleted(Event event) {
        log.info("Handling checkout.session.completed event");

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("Session反序列化失败");
            return;
        }

        Session session = (Session) deserializer.getObject().get();
        String paymentStatus = session.getPaymentStatus();
        String mode = session.getMode();
        Map<String, String> metadata = session.getMetadata();

        if (!"paid".equals(paymentStatus)) {
            return;
        }

        String userId = metadata.get("app_user_id");
        String priceId = metadata.get("price_id");

        if ("subscription".equals(mode)) {
            // 订阅变更事件
            orderService.changeSubscriptionOrder(
                    session.getCustomer(),
                    session.getId(),
                    session.getSubscription(),
                    event
            );
        } else {
            // 充值支付
            orderService.paymentIntentChange(
                    userId,
                    priceId,
                    session.getId(),
                    session.getPaymentIntent(),
                    event
            );
        }
    }

    // ==================== Invoice ====================
    private void handleInvoicePaid(Event event) {
        log.info("Handling invoice.paid event");

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("Invoice反序列化失败");
            return;
        }

        Invoice invoice = (Invoice) deserializer.getObject().get();
        Map<String, String> metadata = invoice.getParent().getSubscriptionDetails().getMetadata();
        String userId = metadata.get("app_user_id");
        String priceId = metadata.get("price_id");


        // ✅ 从 parent.subscription_details 获取 subscription ID
        String subscriptionId = invoice.getParent()
                .getSubscriptionDetails()
                .getSubscription();

        String billReason = invoice.getBillingReason();
        if("subscription_create".equals(billReason) || "subscription_cycle".equals(billReason)) {
            orderService.invoiceChange(
                    userId,
                    priceId,
                    subscriptionId,
                    "",
                    billReason,
                    event
            );
        }
        log.info("Invoice subscriptionId: {} billReason {}", subscriptionId, billReason);
    }

    // ==================== Subscription ====================
    private void handleSubscriptionUpdated(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("Subscription反序列化失败");
            return;
        }

        Subscription subscription = (Subscription) deserializer.getObject().get();
        processSubscription(subscription, event);
    }

    private void processSubscription(Subscription subscription, Event event) {
        Map<String, String> metadata = subscription.getMetadata();

        orderService.subscriptionChange(
                metadata.get("app_user_id"),
                subscription.getStatus(),
                subscription.getCancelAtPeriodEnd(),
                event
        );
    }

    // ==================== Refund ====================
    private void handleChargeRefunded(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("Charge反序列化失败");
            return;
        }

        Charge charge = (Charge) deserializer.getObject().get();
        processChargeRefund(charge, event);
    }

    private void processChargeRefund(Charge charge, Event event) {
        RefundCollection refunds = charge.getRefunds();

        if (refunds == null || refunds.getData() == null || refunds.getData().isEmpty()) {
            log.warn("Charge没有退款记录 - chargeId: {}", charge.getId());
            return;
        }

        List<Refund> refundList = refunds.getData();
        Refund latestRefund = refundList.get(refundList.size() - 1);

        if (latestRefund != null && "succeeded".equals(latestRefund.getStatus())) {
            log.info("✅ 退款成功 - refundId: {}", latestRefund.getId());
            orderService.refundChange(latestRefund.getId(), event);
        }
    }

    private void handleChargeRefundUpdated(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isEmpty()) {
            log.error("Refund反序列化失败");
            return;
        }

        Refund refund = (Refund) deserializer.getObject().get();
        processRefund(refund, event);
    }

    private void processRefund(Refund refund, Event event) {
        if ("succeeded".equals(refund.getStatus())) {
            log.info("✅ 退款更新成功 - refundId: {}", refund.getId());
            orderService.refundChange(refund.getId(), event);
        }
    }

    // ==================== 取消订阅 ====================
    @Override
    public Boolean cancelSubscription(Integer userId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Order order = orderManager.selectByUserIdAndType(userId, OrderTypeEnum.SUBSCRIPTION);
        if (order == null) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has no subscription order");
        }

        Subscription subscription = Subscription.retrieve(order.getStripeInvoiceId());
        Subscription canceledSubscription = subscription.cancel();

        if ("canceled".equals(canceledSubscription.getStatus())) {
            log.info("Subscription canceled: {}", canceledSubscription.getId());
            orderService.cancelSubscription(userId);
            return true;
        }

        log.error("Error canceling subscription: {}", canceledSubscription.getStatus());
        return false;
    }

    // ==================== 退款 ====================
    @Override
    public Boolean confirmRefundRecharge(String refundOrderId) throws StripeException {
        OrderRefund orderRefund = orderRefundManager.selectByUuid(refundOrderId);
        if (orderRefund == null || orderRefund.getStatus() != 1) {
            throw new BaseException(UserErrorType.ORDER_CLOSED, "Invalid refund order");
        }

        Order order = orderManager.selectById(orderRefund.getOrderId());
        Stripe.apiKey = stripeApiKey;

        RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(order.getStripePaymentIntentId())
                .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                .setAmount(orderRefund.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .putMetadata("refund_request_id", orderRefund.getUuid())
                .putMetadata("processed_by", "system")
                .build();

        Refund refund = Refund.create(params);

        if ("succeeded".equals(refund.getStatus()) || "pending".equals(refund.getStatus())) {
            log.info("退款创建成功: {}", refund.getId());
            orderRefund.setStatus(2);
            orderRefund.setStripeRefundId(refund.getId());
            orderRefundManager.update(orderRefund);
            return true;
        }

        log.info("退款失败: {}", refund.getStatus());
        return false;
    }

    @Override
    public Boolean confirmRefundSubscription(String refundOrderId) throws StripeException {
        OrderRefund orderRefund = orderRefundManager.selectByUuid(refundOrderId);
        if (orderRefund == null || orderRefund.getStatus() != 1) {
            throw new BaseException(UserErrorType.ORDER_CLOSED, "Invalid refund order");
        }

        Order order = orderManager.selectById(orderRefund.getOrderId());
        Stripe.apiKey = stripeApiKey;

        // 获取订阅
        Subscription subscription = Subscription.retrieve(order.getStripeInvoiceId());

        // 获取最近的发票
        InvoiceListParams invoiceParams = InvoiceListParams.builder()
                .setSubscription(order.getStripeInvoiceId())
                .setLimit(1L)
                .build();
        InvoiceCollection invoices = Invoice.list(invoiceParams);

        if (invoices.getData().isEmpty()) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "No invoice found for subscription");
        }

        // 取消订阅
        Subscription canceledSubscription = subscription.cancel();

        if ("canceled".equals(canceledSubscription.getStatus())) {
            log.info("订阅已取消: {}", canceledSubscription.getId());

            // 创建退款
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setCharge(order.getStripeChargeId())
                    .setAmount(orderRefund.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                    .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                    .putMetadata("subscription_id", order.getStripeInvoiceId())
                    .putMetadata("refund_type", "refund-subscription")
                    .build();

            Refund refund = Refund.create(refundParams);

            if ("succeeded".equals(refund.getStatus()) || "pending".equals(refund.getStatus())) {
                log.info("退款创建成功: {}", refund.getId());
                orderRefund.setStatus(2);
                orderRefund.setStripeRefundId(refund.getId());
                orderRefundManager.update(orderRefund);
                return true;
            }
        }

        return false;
    }
}