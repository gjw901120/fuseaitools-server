package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/callback/stripe")
@Slf4j
public class StripeCallbackController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/webhook")
    public String handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            return stripeService.handleStripeWebhook(payload, sigHeader);
        } catch (StripeException e) {
            log.info("Stripe webhook payload:{} sigHeader:{} error: {}", payload, sigHeader, e);
            return "failed";
        }
    }

}
