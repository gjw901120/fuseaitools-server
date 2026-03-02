package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.stripe.CreateSessionDTO;
import com.fuse.ai.server.web.model.dto.response.CreateSessionResponse;
import com.stripe.exception.StripeException;

public interface StripeService {

    CreateSessionResponse createSession(CreateSessionDTO createSessionDTO, Integer userId) throws StripeException;

    String handleStripeWebhook(String payload, String sigHeader) throws StripeException;

    Boolean confirmRefundRecharge(String refundOrderId) throws StripeException;

    Boolean confirmRefundSubscription(String refundOrderId) throws StripeException;

    Boolean cancelSubscription(Integer userId) throws StripeException;
}
