package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.Order;
import com.fuse.ai.server.manager.enums.OrderTypeEnum;

public interface OrderManager {

    Integer insert(Order order);

    Integer updateById(Order order);

    Order selectById(Integer id);

    Order selectByUserId(Integer userId);

    Order selectByStripePaymentIntentId(String stripePaymentIntentId);

    Order selectByStripeOrderId(String stripeOrderId);

    Order selectByStripeInvoiceId(String stripeInvoiceId);

    Order selectByUserIdAndType(Integer userId, OrderTypeEnum type);

}