package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.entity.OrderRefund;

public interface OrderRefundManager {

    void insert(OrderRefund orderRefund);

    OrderRefund selectByUuid(String uuid);

    OrderRefund selectByOrderId(Integer orderId);

    OrderRefund selectByStripeRefundId(String stripeRefundId);

    void update(OrderRefund orderRefund);

}
