package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.*;
import com.fuse.ai.server.manager.enums.OrderStatusEnum;
import com.fuse.ai.server.manager.enums.OrderTypeEnum;
import com.fuse.ai.server.manager.enums.SubscriptionPackageEnum;
import com.fuse.ai.server.manager.enums.SubscriptionTypeEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.web.common.utils.FeishuMessageUtil;
import com.fuse.ai.server.web.model.vo.OrderRefundVO;
import com.fuse.ai.server.web.service.OrderService;
import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.UserErrorType;
import com.stripe.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private OrderRefundManager orderRefundManager;

    @Autowired
    private SubscriptionConfigManager subscriptionConfigManager;

    @Autowired
    private RechargeConfigManager rechargeConfigManager;

    @Autowired
    private UserCreditsManager userCreditsManager;

    @Autowired
    private SubscriptionManager subscriptionManager;

    @Autowired
    private SubscriptionPlanManager subscriptionPlanManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private OrderLogManager orderLogManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRechargeOrder(String customer, String paymentIntent, String chargeId, Event event) {
        User user = userManager.selectByStripeCustomerId(customer);
        //查询判断是否已存在订单
        Order order = orderManager.selectByStripePaymentIntentId(paymentIntent);
        if(order == null) {
            Order newOrder = Order.create(
                    user.getId(),
                    OrderTypeEnum.TOP_UP,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    OrderStatusEnum.UNPAID,
                    "",
                    paymentIntent,
                    customer,
                    "",
                    chargeId
            );
            orderManager.insert(newOrder);
            orderLogManager.insert(OrderLog.create(newOrder.getId(), event.toJson()));
        } else {
            order.setStripeChargeId(chargeId);
            orderManager.updateById(order);
            orderLogManager.insert(OrderLog.create(order.getId(), event.toJson()));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubscriptionOrder(String customer, String stripeOrderId, String paymentIntent, String chargeId, Event event) {
        User user = userManager.selectByStripeCustomerId(customer);
        //查询判断是否已存在订单
        Order order = orderManager.selectByStripeOrderId(stripeOrderId);
        if(order == null) {
            Order newOrder = Order.create(
                    user.getId(),
                    OrderTypeEnum.SUBSCRIPTION,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    OrderStatusEnum.UNPAID,
                    stripeOrderId,
                    paymentIntent,
                    customer,
                    "",
                    chargeId
            );
            orderManager.insert(newOrder);
            orderLogManager.insert(OrderLog.create(newOrder.getId(), event.toJson()));
        } else {
            order.setStripePaymentIntentId(paymentIntent);
            order.setStripeChargeId(chargeId);
            orderManager.updateById(order);
            orderLogManager.insert(OrderLog.create(order.getId(), event.toJson()));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSubscriptionOrder(String customer, String stripeOrderId,String subscriptionId, Event event) {
        User user = userManager.selectByStripeCustomerId(customer);
        //查询判断是否已存在订单
        Order order = orderManager.selectByStripeOrderId(stripeOrderId);
        if(order == null) {
            Order newOrder = Order.create(
                    user.getId(),
                    OrderTypeEnum.SUBSCRIPTION,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    OrderStatusEnum.UNPAID,
                    stripeOrderId,
                    "",
                    customer,
                    subscriptionId,
                    ""
            );
            orderManager.insert(newOrder);
            orderLogManager.insert(OrderLog.create(newOrder.getId(), event.toJson()));
        } else {
            order.setStripeInvoiceId(subscriptionId);
            orderManager.updateById(order);
            orderLogManager.insert(OrderLog.create(order.getId(), event.toJson()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentIntentChange(String uuid, String priceId, String stripeOrderId, String paymentIntentId, Event event) {
        User user = userManager.selectByUuid(uuid);
        user.setIsTopUp(1);
        userManager.updateById(user);
        RechargeConfig rechargeConfig = rechargeConfigManager.getDetailByStripePriceId(priceId);
        if(rechargeConfig == null) {
            log.error("RechargeConfig not found by stripe price id: {}", priceId);
            return;
        }
        Order order = orderManager.selectByStripePaymentIntentId(paymentIntentId);
        //存在修改，不存在创建
        if(order == null ) {
            order = Order.create(
                    user.getId(),
                    OrderTypeEnum.TOP_UP,
                    0,
                    rechargeConfig.getCost(),
                    BigDecimal.ZERO,
                    rechargeConfig.getCost(),
                    BigDecimal.ZERO,
                    OrderStatusEnum.PAID,
                    stripeOrderId,
                    paymentIntentId,
                    user.getStripeCustomerId(),
                    "",
                    ""
            );
            orderManager.insert(order);
        } else {
            if(!OrderStatusEnum.UNPAID.equals(order.getStatus())) {
                log.error("Order not found or order status is not UNPAID {}", order);
                return;
            }
            order.setConfigId(rechargeConfig.getId());
            order.setPaymentAmount(rechargeConfig.getCost());
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setStripeOrderId(stripeOrderId);
            order.setAmount(rechargeConfig.getCost());
            order.setStatus(OrderStatusEnum.PAID);
            order.setStripeCustomerId(user.getStripeCustomerId());
            orderManager.updateById(order);
        }

        UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(user.getId(), 1);

        if(userCredits == null) {
            userCredits = UserCredits.create(
                    user.getId(),
                    rechargeConfig.getTotalCredits(),
                    BigDecimal.ZERO,
                    1,
                    1
            );
            userCreditsManager.insert(userCredits);
        } else {
            userCredits.setCredits(userCredits.getCredits().add(rechargeConfig.getTotalCredits()));
            userCreditsManager.updateById(userCredits);
        }

        FeishuMessageUtil.sendRechargeSubscribeMessage("充值订单付款成功, 金额：".concat(rechargeConfig.getCost().toString()));

        orderLogManager.insert(OrderLog.create(order.getId(), event.toJson()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invoiceChange(String uuid, String priceId, String subscriptionId, String stripeOrderId, String invoiceBillingReason, Event event) {
        User user = userManager.selectByUuid(uuid);
        user.setIsSubscription(1);
        userManager.updateById(user);
        SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailByStripePriceId(priceId);
        if(subscriptionConfig == null) {
            log.error("SubscriptionConfig not found by stripe price id: {}", priceId);
            return;
        }
        Order order = orderManager.selectByStripeInvoiceId(subscriptionId);
        //未存在的订单|订单不是未支付状态进行初次订阅行为|订单不是已支付状态进行续订行为
        if(order == null || (!OrderStatusEnum.UNPAID.equals(order.getStatus()) && "subscription_create".equals(invoiceBillingReason))
            || (!OrderStatusEnum.PAID.equals(order.getStatus()) && "subscription_cycle".equals(invoiceBillingReason))) {
            log.error("Order not found or order status is not UNPAID {}", order);
            return;
        }
        //首次订阅行为->完善订单->充值credits
        if("subscription_create".equals(invoiceBillingReason)) {
            order.setConfigId(subscriptionConfig.getId());
            order.setPaymentAmount(subscriptionConfig.getCost());
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setAmount(subscriptionConfig.getCost());
            order.setStatus(OrderStatusEnum.PAID);
            order.setStripeInvoiceId(subscriptionId);
            order.setStripeCustomerId(user.getStripeCustomerId());
            orderManager.updateById(order);
            UserCredits userCredits = UserCredits.create(
                    user.getId(),
                    subscriptionConfig.getTotalCredits(),
                    BigDecimal.ZERO,
                    2,
                    1
            );
            userCreditsManager.insert(userCredits);
        } else {
            //续订行为->修改用户credits状态&&新增新期credits
            UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(user.getId(), 2);
            userCredits.setStatus(2);
            userCreditsManager.updateById(userCredits);
            UserCredits newUserCredits = UserCredits.create(
                    user.getId(),
                    subscriptionConfig.getTotalCredits(),
                    BigDecimal.ZERO,
                    2,
                    1
            );
            userCreditsManager.insert(newUserCredits);
        }

        LocalDate startDate = LocalDate.now();

        LocalDate endDate = LocalDate.now();

        //更新订阅记录
        Subscription subscription = Subscription.create(
                order.getId(),
                user.getId(),
                subscriptionId,
                SubscriptionTypeEnum.of(subscriptionConfig.getType()),
                1,
                SubscriptionPackageEnum.of(subscriptionConfig.getSubscriptionPackage()),
                startDate,
                endDate
        );

        //处理周订阅/月订阅/年订阅
        if(subscriptionConfig.getType().equals(SubscriptionTypeEnum.WEEKLY.getCode())) {
            endDate = startDate.plusWeeks(1);
            subscription.setEndDate(endDate);
            subscriptionManager.insert(subscription);
            subscriptionPlanManager.insert(SubscriptionPlan.create(
                    user.getId(),
                    subscription.getId(),
                    startDate,
                    endDate,
                    subscriptionConfig.getCredits(),
                    subscriptionConfig.getGiftCredits(),
                    1

            ));

        } else if(subscriptionConfig.getType().equals(SubscriptionTypeEnum.MONTHLY.getCode())) {
            endDate = startDate.plusMonths(1);
            subscription.setEndDate(endDate);
            subscriptionManager.insert(subscription);
            subscriptionPlanManager.insert(SubscriptionPlan.create(
                    user.getId(),
                    subscription.getId(),
                    startDate,
                    endDate,
                    subscriptionConfig.getCredits(),
                    subscriptionConfig.getGiftCredits(),
                    1
            ));
        } else if(subscriptionConfig.getType().equals(SubscriptionTypeEnum.YEARLY.getCode())) {
            endDate = startDate.plusYears(1);
            subscription.setEndDate(endDate);
            subscriptionManager.insert(subscription);
            List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                subscriptionPlans.add(SubscriptionPlan.create(
                        user.getId(),
                        subscription.getId(),
                        startDate.plusMonths(i),
                        startDate.plusMonths(i + 1),
                        subscriptionConfig.getCredits(),
                        subscriptionConfig.getGiftCredits(),
                        1
                ));
            }
            subscriptionPlanManager.insertBatch(subscriptionPlans);
        }
        FeishuMessageUtil.sendRechargeSubscribeMessage("订阅订单付款成功, 金额：".concat(subscriptionConfig.getCost().toString()));
        orderLogManager.insert(OrderLog.create(order.getId(), event.toJson()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscriptionChange(String uuid, String status, boolean isCancel, Event event) {
        User user = userManager.selectByUuid(uuid);
        //订阅用户，并且用户取消订阅或者订阅到期->修改订阅状态
        if(1 == user.getIsSubscription() && ("canceled".equals(status) || ("active".equals(status) && isCancel))) {
            //取消订阅
            user.setIsSubscription(0);
            userManager.updateById(user);
        } else {
            return;
        }
        orderLogManager.insert(OrderLog.create(0, event.toJson()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundChange(String refundId, Event event) {
        log.info("Refund change for refund id {} with event {}", refundId, event);
        OrderRefund orderRefund = orderRefundManager.selectByStripeRefundId(refundId);
        if(orderRefund == null || orderRefund.getStatus() != 2) {
            log.error("OrderRefund not found by stripe refund id: {}", refundId);
            return;
        }
        Order order = orderManager.selectById(orderRefund.getOrderId());
        if(order == null) {
            log.error("Order not found by id: {}", orderRefund.getOrderId());
            return;
        }
        User user = userManager.selectById(order.getUserId());
        //判断充值还是订阅
        if(order.getType() != OrderTypeEnum.TOP_UP) {
            //充值，扣除当前账号的credits，并初始化充值状态
            UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(order.getUserId(), 1);
            userCredits.setCredits(BigDecimal.ZERO);
            userCreditsManager.updateById(userCredits);
            user.setIsTopUp(0);
        } else {
            //订阅，扣除当前账号的credits，并初始化订阅状态
            UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(order.getUserId(), 2);
            userCredits.setCredits(BigDecimal.ZERO);
            userCreditsManager.updateById(userCredits);
            Subscription subscription = subscriptionManager.selectByOrderId(order.getId());
            subscription.setStatus(2);
            subscriptionManager.update(subscription);  //修改订阅表
            List<SubscriptionPlan> subscriptionPlans = subscriptionPlanManager.selectBySubscriptionId(subscription.getId());
            //修改订阅计划表
            subscriptionPlanManager.updateStatusByIds(subscriptionPlans.stream().map(SubscriptionPlan::getId).collect(Collectors.toList()), 2);
            user.setIsSubscription(0);
        }
        userManager.updateById(user);
        orderRefund.setStatus(3);
        orderRefundManager.update(orderRefund);
        order.setStatus(OrderStatusEnum.REFUNDED);
        orderManager.updateById(order);
        orderLogManager.insert(OrderLog.create(orderRefund.getOrderId(), event.toJson()));
    }

    @Override
    public void cancelSubscription(Integer userId) {
        User user = userManager.selectById(userId);
        user.setIsSubscription(0);
        userManager.updateById(user);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRefundVO refundRecharge(Integer userId, Boolean isConfirm) {
        Order order = orderManager.selectByUserIdAndType(userId, OrderTypeEnum.TOP_UP);
        if(order == null) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has recharge order");
        }
        RechargeConfig rechargeConfig = rechargeConfigManager.getDetailById(order.getConfigId());
        UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userId, 1);
        //判断用户剩余credits扣除赠送后的剩余，退还，如果没有剩余则不可发起退款
        BigDecimal reallyCredits = userCredits.getCredits().subtract(rechargeConfig.getGiftCredits());
        if(reallyCredits.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has no remaining credits");
        }
        //实际剩余credits/充值的credits*订单金额=退款金额
        BigDecimal refundAmount = order.getAmount().multiply(reallyCredits.divide(rechargeConfig.getCredits(), 2, RoundingMode.HALF_UP));
        if(isConfirm) {
            OrderRefund orderRefund = OrderRefund.create(
                    order.getId(),
                    "",
                    1,
                    2,
                    refundAmount,
                    ""
            );
            orderRefundManager.insert(orderRefund);
        }
        OrderRefundVO orderRefundVO = new OrderRefundVO();
        orderRefundVO.setRefundAmount(refundAmount);
        return orderRefundVO;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderRefundVO refundSubscription(Integer userId, Boolean isConfirm) {
        Order order = orderManager.selectByUserIdAndType(userId, OrderTypeEnum.SUBSCRIPTION);
        if(order == null) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has no subscription order");
        }
        SubscriptionConfig subscriptionConfig = subscriptionConfigManager.getDetailById(order.getConfigId());
        UserCredits userCredits = userCreditsManager.getDetailByUserIdAndType(userId, 2);
        Subscription subscription = subscriptionManager.selectByUserId(userId);
        //判断用户剩余credits扣除赠送后的剩余，退还，如果没有剩余则不可发起退款->年费需要计算剩余月份
        BigDecimal reallyCredits;
        BigDecimal totalCredits = subscriptionConfig.getCredits();
        if (subscriptionConfig.getType().equals(SubscriptionTypeEnum.YEARLY.getCode())) {
            int monthsDifference = getCeilingMonthDifferenceAlternative(subscription.getStartDate());
            reallyCredits = userCredits.getCredits().subtract(subscriptionConfig.getGiftCredits());
            if(monthsDifference < 12) {
                reallyCredits = subscriptionConfig.getCredits().multiply(BigDecimal.valueOf(monthsDifference)).add(reallyCredits);
            }
            totalCredits = totalCredits.multiply(BigDecimal.valueOf(12));
        } else {
            reallyCredits = userCredits.getCredits().subtract(subscriptionConfig.getGiftCredits());
        }

        if(reallyCredits.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BaseException(UserErrorType.USER_CLIENT_ERROR, "User has no remaining credits");
        }
        BigDecimal refundAmount = order.getAmount().multiply(reallyCredits.divide(totalCredits, 2, RoundingMode.HALF_UP));
        if(isConfirm) {
            OrderRefund orderRefund = OrderRefund.create(
                    order.getId(),
                    "",
                    1,
                    2,
                    refundAmount,
                    ""
            );
            orderRefundManager.insert(orderRefund);
        }

        OrderRefundVO orderRefundVO = new OrderRefundVO();
        orderRefundVO.setRefundAmount(refundAmount);
        return orderRefundVO;
    }

    /*
     * 刷新订阅计划，每日0点执行，将昨日到期的订阅credits失效，将今日生效的订阅credits添加，返回所有受影响的订阅计划列表
     * @return 受影响的订阅计划列表
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SubscriptionPlan> refreshPlan() {
        //获取今日日期
        LocalDate today = LocalDate.now();
        //获取昨日
        LocalDate yesterday = today.minusDays(1);
        //失效昨日到期订阅credits
        List<SubscriptionPlan> expiredPlans = subscriptionPlanManager.selectByEndDate(yesterday);
        //失效之前的credits
        if(expiredPlans == null || expiredPlans.isEmpty()) {
            return Collections.emptyList();
        }
        for (SubscriptionPlan expiredPlan : expiredPlans) {
            userCreditsManager.updateStatusByUserId(expiredPlan.getUserId(), 2);
        }
        List<Integer> expiredUserIds = expiredPlans.stream().map(SubscriptionPlan::getUserId).toList();
        subscriptionPlanManager.updateStatusByIds(expiredPlans.stream().map(SubscriptionPlan::getId).collect(Collectors.toList()), 2);
        //进日生效任务添加credits
        List<SubscriptionPlan> todayPlans = subscriptionPlanManager.selectByStartDate(today);
        if(todayPlans == null || todayPlans.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> newUserIds = todayPlans.stream().map(SubscriptionPlan::getUserId).toList();
        for (SubscriptionPlan todayPlan : todayPlans) {
            userCreditsManager.insert(
                    UserCredits.create(
                            todayPlan.getUserId(),
                            todayPlan.getCredits().add(todayPlan.getGiftCredits()),
                            BigDecimal.ZERO,
                            2, 1
                    )
            );
        }
        //对比本期订阅计划已结束，并且没有新订阅的用户，将is_subscription设置为0
        List<Integer> diffUserIds = expiredUserIds.stream().filter(userId -> !newUserIds.contains(userId)).toList();
        for (Integer diffUserId : diffUserIds) {
            userManager.updateIsSubscriptionByUserId(diffUserId, 0);
        }

        return Stream.concat(expiredPlans.stream(), todayPlans.stream()).collect(Collectors.toList());
    }

    private static int getCeilingMonthDifferenceAlternative(LocalDate startDate) {
        LocalDate today = LocalDate.now();

        // 1. 计算完整的月份差（向下取整）
        long wholeMonthsBetween = ChronoUnit.MONTHS.between(startDate, today);

        // 2. 获取比较所需的“日”
        int startDayOfMonth = startDate.getDayOfMonth();
        int todayDayOfMonth = today.getDayOfMonth();

        // 3. 判断是否需要向上取整
        // 核心逻辑：如果起始日期的“日”大于今天的“日”，说明这个月还没“满月”
        // 例如：startDate是11-22，今天是02-08， 22 > 8，所以2个完整月+未满的1个月 = 3个月
        boolean needsCeiling = startDayOfMonth > todayDayOfMonth;

        // 4. 应用向上取整
        long resultMonths = needsCeiling ? wholeMonthsBetween + 1 : wholeMonthsBetween;

        // 5. 确保结果非负（如果 startDate 在今天之后，则相差为0）
        return (int) Math.max(0, resultMonths);
    }



}
